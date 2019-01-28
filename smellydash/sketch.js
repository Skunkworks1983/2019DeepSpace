const remote = require('electron').remote;
const console = remote.getGlobal('console');
console.log("sketch.js got global console");
const ntClient = remote.getGlobal('ntClient');

const Vector2 = require('./src/vector2');
const Bezier = require('./src/bezier');
const Pose = require('./src/pose');

// ---------- Variable Init ----------
var img; // Field image
var poses = []; // Pathing poses

var wasConnected = true; // true so it will trigger the reconnect
var wasPressed = false;

// Detects if the mouse is currently in the canvas, to prevent triggering pathing
// Functions when pressing buttons
function mouseIsInCanvas() {
    return 0 <= mouseX && mouseX < CANVAS_WIDTH && 0 <= mouseY && mouseY < CANVAS_HEIGHT;
}

// Trys to connect to the network tables
function ntConnect() {
    console.log("trying to connect");

    // Connects the client to the server on team 1983's roborio
    // We do this as early as possible so it has time to connect while the
    // rendering process loads
    ntClient.start((isConnected, err) => {
        // Displays the error and the state of connection
        console.log({ isConnected, err });
    }, '10.19.83.2');
}

// Called when we successfully connect to the network table. Updates buttons
function connected () {
    if(!wasConnected)
    {
        console.log("connected!");
        document.getElementById("retryconnect").style.display = "none";
        document.getElementById("connectionstatus").style.backgroundColor = "green";
        wasConnected = true;
    }
}

// Called when we disconnect to the network table. Updates buttons
function notconnected() {
    if(wasConnected)
    {
        console.log("disconnected :(")
        document.getElementById("retryconnect").style.display = "inline-block";
        document.getElementById("connectionstatus").style.backgroundColor = "red";
        document.getElementById("coords").textContent = " ";
        wasConnected = false;
    }
}

// ---------- preload ----------

// Called before window even starts rendering
function preload() {
    console.log("preload...");
    img = loadImage('resources/cropped_field.png');

    notconnected();
    ntConnect();

    document.getElementById("retryconnect").onclick = ntConnect;
    document.getElementById("sendpath").onclick = function () {
        console.log("sending a path");
        var pathString = poses[0];

        for(var i = 1; i < poses.length; i++) {
            pathString = pathString + ":" + poses[i];
        }

        ntClient.Update(ntClient.getKeyID("/SmartDashboard/path"), pathString);
        ntClient.Update(ntClient.getKeyID("/SmartDashboard/gotPath"), "false");
        console.log(pathString);
    };
    document.getElementById("clearpath").onclick = function () {
        poses = [];

        document.getElementById("pathbuttons").style.display = "none";
    };
    document.getElementById("removepose").onclick = function () {
        poses.splice(-1); // Delete last element

        if(poses.length === 0) {
            document.getElementById("pathbuttons").style.display = "none";
        }
    }

    document.getElementById("pathbuttons").style.display = "none";

    console.log("preload complete")
}

// ---------- setup ----------

// Called when window starts rendering
function setup() {
    console.log("setup...");
    createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    img.resize(0, CANVAS_HEIGHT);
    angleMode(DEGREES);
    frameRate(50);
    console.log("setup complete");
}

// Called every frame
function draw() {

    // ---------- nt getting and coord updating ----------
    if(ntClient.isConnected() && ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotX")) != undefined) {
        connected();
        x = precise(ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotX")).val);
        y = precise(ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotY")).val);
        heading = precise(ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotAngle")).val);
        document.getElementById("coords").textContent = "X: " + x +
        " Y: " + y + " \u0398: " + heading;
    }
    else {
        notconnected();
    }

    clear();
    rectMode(CENTER);
    image(img, 0, 0);

    debounced = debounceMouse();

    // Create pose
    if(debounced) {
        if(poses.length === 0) {
            document.getElementById("pathbuttons").style.display = "inline-block";
        }
        poses[poses.length] = new Pose(mouseX, mouseY, 0);
    }
    // Rotate pose
    if(!debounced && mouseIsPressed && mouseIsInCanvas()) {
        poses[poses.length - 1].heading = -atan2(mouseY - poses[poses.length - 1].position.y,
            mouseX - poses[poses.length - 1].position.x);

            // Draw mouse location
            fill(0, 255, 0)
            ellipse(mouseX, mouseY, 5);
    }

    // Draw poses
    fill(0, 0, 255);
    stroke(0);
    strokeWeight(1);
    poses.forEach(pose => {
        push();

        translate(pose.position.x, pose.position.y);
        rotate(-(pose.heading + 90));
        rect(0, 0, ROBOT_WIDTH, ROBOT_HEIGHT)

        fill(0);
        triangle(0, 5, 5, 0, -5, 0);

        pop();
    });

    // Draw path
    fill(0, 0, 0, 0);
    strokeWeight(2);
    poses.forEach(pose => {
        let index = poses.indexOf(pose);
        if(index < poses.length - 1) {
            nextPose = poses[index + 1];

            let cp0 = Vector2.scale(pose.forward, TANGENT_LENGTH * PIXELS_PER_FOOT);
            let cp1 = Vector2.scale(nextPose.forward, -TANGENT_LENGTH * PIXELS_PER_FOOT);

            let points = [
                new Vector2(pose.position.x, pose.position.y),
                new Vector2(pose.position.x + cp0.x, pose.position.y + cp0.y),
                new Vector2(nextPose.position.x + cp1.x, nextPose.position.y + cp1.y),
                new Vector2(nextPose.position.x, nextPose.position.y)
            ];

            bezier(
                points[0].x, points[0].y,
                points[1].x, points[1].y,
                points[2].x, points[2].y,
                points[3].x, points[3].y
            );

            // Draw track
            let resolution = 20; // should be constant but test
            for(let offset = -ROBOT_WIDTH/2; offset < ROBOT_WIDTH; offset += ROBOT_WIDTH/2) {
                for(let index = 0; index < resolution; index++) {
                    let t = index / resolution;

                    let p0 = Bezier.offset(index / resolution, offset, points);
                    let p1 = Bezier.offset((index + 1) / resolution, offset, points);

                    line(p0.x, p0.y, p1.x, p1.y);
                }
            }
        }
    })

    // Draw pose text
    fill(255);
    stroke(0);
    poses.forEach(pose => text(pose, pose.position.x, pose.position.y));
}
