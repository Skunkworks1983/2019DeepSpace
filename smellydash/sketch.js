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

var robot = new Pose(0, 0, 90); // Default robot pose
var lookahead = new Vector2(-1, -1);
var closestPoint = new Vector2(-1, -1);

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

        // If there is a lookahead point in the network table, read value
        let entryLookaheadX = ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/lookaheadX"));
        lookahead.x = typeof entryLookaheadX == 'undefined' ? 0 : entryLookaheadX.val;
        let entryLookaheadY = ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/lookaheadY"));
        lookahead.y = typeof entryLookaheadY == 'undefined' ? 0 : entryLookaheadY.val;

        // If there is a closestPoint point in the network table, read value
        let entryClosestPointX = ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/closestPointX"));
        closestPoint.x = typeof entryClosestPointX == 'undefined' ? 0 : entryClosestPointX.val;
        let entryClosestPointY = ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/closestPointY"));
        closestPoint.y = typeof entryClosestPointY == 'undefined' ? 0 : entryClosestPointY.val;
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

    // Draw locations
    var LENGTH = ROBOT_HEIGHT / PIXELS_PER_FOOT;

    push();

    scale(PIXELS_PER_FOOT, -PIXELS_PER_FOOT);
    translate(0, -27);

    strokeWeight(0);
    fill(255, 0, 0);

    // Rockets
    var dx = LENGTH / 2.0 * Math.cos(61.25 * Math.PI / 180.0);
    var dy = LENGTH / 2.0 * Math.sin(61.25 * Math.PI / 180.0);

    arrow(1 + (6.5 / 12.0) + dx, 17 + (8.5 / 12.0) - dy, 118.75); // LEFT_ROCKET_CLOSE
    arrow(2 + (3.5 / 12.0) + LENGTH / 2.0, 19, 180); // LEFT_ROCKET_MIDDLE
    arrow(1 + (6.5 / 12.0) + dx, 20 + (3.5 / 12.0) + dy, -118.75); // LEFT_ROCKET_FAR

    arrow(25 + (5.5 / 12.0) - dx, 17 + (8.5 / 12.0) - dy, 61.25); // RIGHT_ROCKET_CLOSE
    arrow(24 + (8.5 / 12.0) - LENGTH / 2.0, 19, 0); // RIGHT_ROCKET_MIDDLE
    arrow(25 + (5.5 / 12.0) - dx, 20 + (3.5 / 12.0) + dy, -61.25); // RIGHT_ROCKET_FAR

    // Cargo ship
    arrow(11 + (7.25 / 12.0) - (5.125 / 12.0) - LENGTH / 2.0, 25 + (7.5 / 12.0), 0); // CARGO_SHIP_LEFT_CLOSE
    arrow(11 + (7.25 / 12.0) - (5.125 / 12.0) - LENGTH / 2.0, 23 + (7.375 / 12.0), 0); // CARGO_SHIP_LEFT_MIDDLE
    arrow(11 + (7.25 / 12.0) - (5.125 / 12.0) - LENGTH / 2.0, 21 + (11.25 / 12.0), 0); // CARGO_SHIP_LEFT_FAR
    arrow(15 + (4.75 / 12.0) + (5.125 / 12.0) + LENGTH / 2.0, 25 + (7.5 / 12.0), 180); // CARGO_SHIP_RIGHT_CLOSE
    arrow(15 + (4.75 / 12.0) + (5.125 / 12.0) + LENGTH / 2.0, 23 + (7.375 / 12.0), 180); // CARGO_SHIP_RIGHT_MIDDLE
    arrow(15 + (4.75 / 12.0) + (5.125 / 12.0) + LENGTH / 2.0, 21 + (11.25 / 12.0), 180); // CARGO_SHIP_RIGHT_FAR
    arrow(12 + (7 / 12.0), 18 + (10.875 / 12.0) - (7.5 / 12.0) - LENGTH / 2.0, 90); // CARGO_SHIP_MIDDLE_LEFT
    arrow(14 + (5 / 12.0), 18 + (10.875 / 12.0) - (7.5 / 12.0) - LENGTH / 2.0, 90); // CARGO_SHIP_MIDDLE_RIGHT

    // Loading stations
    arrow(1 + (10.75 / 12.0), LENGTH / 2.0, -90); // LEFT_LOADING_STATION
    arrow(25 + (1.25 / 12.0), LENGTH / 2.0, -90); // RIGHT_LOADING_STATION

    // HAB
    arrow(9 + (8 / 12.0), LENGTH / 2.0, 90); // LEVEL_1
    arrow(17 + (4 / 12.0), LENGTH / 2.0, 90); // LEFT_LEVEL_2
    arrow(13 + (6 / 12.0), 4 + LENGTH / 2.0, 90); // RIGHT_LEVEL_2

    pop();

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
    strokeWeight(2);
    poses.forEach(pose => pose.showText());

    // Draw robot
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

    // Draw lookahead
    push();

    fill(0, 255, 0);
    stroke(0);
    strokeWeight(1);
    translate(lookahead.x * PIXELS_PER_FOOT, (27 - lookahead.y) * PIXELS_PER_FOOT);
    ellipse(0, 0, 10);

    // stroke(0, 255, 0);
    // strokeWeight(5);
    // line(0, 0, robot.position.x * PIXELS_PER_FOOT, robot.position.y * PIXELS_PER_FOOT);

    pop();

    // Draw closest point
    push();

    fill(0, 0, 255);
    stroke(0);
    strokeWeight(1);
    translate(closestPoint.x * PIXELS_PER_FOOT, (27 - closestPoint.y) * PIXELS_PER_FOOT);
    ellipse(0, 0, 10);

    // stroke(0, 255, 0);
    // strokeWeight(5);
    // line(0, 0, robot.position.x * PIXELS_PER_FOOT, robot.position.y * PIXELS_PER_FOOT);

    pop();

    // broken cuz reasons
    fill(255);
    stroke(0);
    strokeWeight(1);

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

function arrow(x, y, heading) {
  let size = 0.5;
  let lineWeight = 0.1;

  push();

  translate(x, y);

  ellipse(0, 0, size);

  strokeWeight(0.1);
  rotate(heading);
  line(0, 0, size, 0);

  strokeWeight(0);

  pop();
}
