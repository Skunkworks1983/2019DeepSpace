const remote = require('electron').remote;
const console = remote.getGlobal('console');
console.log("sketch.js got global console");
const ntClient = remote.getGlobal('ntClient');

const Vector2 = require('./src/vector2');
const Bezier = require('./src/bezier');
const Pose = require('./src/pose');
const Path = require('./src/path');

// ---------- Variable Init ----------
var img; // Field image
var poses = []; // Pathing poses

var wasConnected = true; // true so it will trigger the reconnect
var wasPressed = false; // Whether the mouse was pressed last frame (for debounce)
var poseDragging = null; // The pose that is being dragged

var robot = new Pose(5, 5, 45); // Default robot pose

// Detects if the mouse is currently in the canvas, to prevent triggering pathing
//functions when pressing buttons
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
        wasConnected = false;
    }
}

// ---------- preload ----------

// Called before window even starts rendering
function preload() {
    console.log("preload...");
    img = loadImage('resources/field.png');

    notconnected();
    ntConnect();

    document.getElementById("retryconnect").onclick = ntConnect;
    document.getElementById("sendpath").onclick = function () {
        console.log("sending a path");
        var pathString = poses[0].toString();

        for(var i = 1; i < poses.length; i++) {
            pathString = pathString + ":" + poses[i].toString();
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

    // Only show if there are poses
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
        robot.position.x = ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotX")).val;
        robot.position.y = ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotY")).val;
        robot.heading = ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotAngle")).val;
    }
    else {
        notconnected();
    }

    clear();
    rectMode(CENTER);
    image(img, 0, 0);

    // Rotate pose
    if(poseDragging && mouseIsInCanvas()) {
        if(mouseButton === LEFT)
            poseDragging.position = new Vector2(mouseX, mouseY);

        if(mouseButton === RIGHT)
            poseDragging.heading = -atan2(mouseY - poseDragging.position.y, mouseX - poseDragging.position.x);

        // Draw mouse location
        fill(0, 255, 0)
        ellipse(mouseX, mouseY, 5);
    }

    // Draw poses
    fill(0, 0, 255);
    stroke(0);
    strokeWeight(1);
    poses.forEach(pose => {
        pose.show();
    });

    // Draw path
    fill(0, 0, 0, 0);
    strokeWeight(2);
    Path.show(poses);

    // Draw pose text
    fill(255);
    stroke(0);
    poses.forEach(pose => pose.showText());

    // draw robot
    push();

    fill(255, 0, 0);
    stroke(0);
    strokeWeight(1);

    translate(robot.position.x * PIXELS_PER_FOOT, (27 - robot.position.y) * PIXELS_PER_FOOT);
    rotate(-(robot.heading + 90));
    rect(0, 0, ROBOT_WIDTH, ROBOT_HEIGHT)

    fill(0);
    triangle(0, 5, 5, 0, -5, 0);

    pop();

    fill(255);
    stroke(0);
    // broken cuz reasons
    text(
        robot.position.x.toFixed(2) + "," + robot.position.y.toFixed(2) + "," + robot.heading.toFixed(2)
        , robot.position.x * PIXELS_PER_FOOT, (27 - robot.position.y) * PIXELS_PER_FOOT);
}

function mousePressed() {
    // Do nothing if the mouse isn't in the canvas
    if(!mouseIsInCanvas())
        return;

    if(poses.length > 0) {
        // Create pose
        let mousePosition = new Vector2(mouseX, mouseY);
        let sorted = poses.slice(0);
        sorted.sort((pose1, pose2) => {
            return Vector2.distance(mousePosition, pose1.position) - Vector2.distance(mousePosition, pose2.position);
        });

        // If we left click, create a new pose. Otherwise, select the closest pose
        if(mouseButton === LEFT && Vector2.distance(mousePosition, sorted[0].position) > DRAG_DISTANCE * PIXELS_PER_FOOT) {
            poses[poses.length] = new Pose(mouseX, mouseY, 90);
            poseDragging = poses[poses.length - 1];
        } else {
            poseDragging = sorted[0];
        }
    } else if(mouseButton === LEFT) {
        // Only create a new pose if we left click
        poses[poses.length] = new Pose(mouseX, mouseY, 90);
        poseDragging = poses[poses.length - 1];
    }

    // Show path buttons
    if(poses.length > 0)
        document.getElementById("pathbuttons").style.display = "inline-block";
}

function mouseReleased() {
    poseDragging = null;
}
