const remote = require('electron').remote;
const console = remote.getGlobal('console');
console.log("sketch.js got global console");
const ntClient = remote.getGlobal('ntClient');

const Vector2 = require('./src/vector2');
const Bezier = require('./src/bezier');
const Pose = require('./src/pose');
const Path = require('./src/path');
const Location = require('./src/location');

// ---------- Variable Init ----------
var img; // Field image
var poses = []; // Pathing poses

var wasConnected = true; // true so it will trigger the reconnect
var wasPressed = false; // Whether the mouse was pressed last frame (for debounce)
var poseDragging = null; // The pose that is being dragged

var robot = new Pose(0, 0, 90); // Default robot pose
var lookahead = new Vector2(-1, -1);
var closestPoint = new Vector2(-1, -1);

// Field locations
let LENGTH = ROBOT_HEIGHT / PIXELS_PER_FOOT;
let dx = LENGTH / 2.0 * Math.cos(61.25 * Math.PI / 180.0);
let dy = LENGTH / 2.0 * Math.sin(61.25 * Math.PI / 180.0);

var test = new Location(10, 10, 90);

const LOCATION = {
  // Rockets
  LEFT_ROCKET_CLOSE: new Location("LEFT_ROCKET_CLOSE", 1 + (6.5 / 12.0) + dx, 17 + (8.5 / 12.0) - dy, 118.75),
  LEFT_ROCKET_MIDDLE: new Location("LEFT_ROCKET_MIDDLE", 2 + (3.5 / 12.0) + LENGTH / 2.0, 19, 180),
  LEFT_ROCKET_FAR: new Location("LEFT_ROCKET_FAR", 1 + (6.5 / 12.0) + dx, 20 + (3.5 / 12.0) + dy, -118.75),
  RIGHT_ROCKET_CLOSE: new Location("RIGHT_ROCKET_CLOSE", 25 + (5.5 / 12.0) - dx, 17 + (8.5 / 12.0) - dy, 61.25),
  RIGHT_ROCKET_MIDDLE: new Location("RIGHT_ROCKET_MIDDLE", 24 + (8.5 / 12.0) - LENGTH / 2.0, 19, 0),
  RIGHT_ROCKET_FAR: new Location("RIGHT_ROCKET_FAR", 25 + (5.5 / 12.0) - dx, 20 + (3.5 / 12.0) + dy, -61.25),

  LEFT_ROCKET_FAR_DRIVER_SWITCH: new Location("LEFT_ROCKET_FAR_DRIVER_SWITCH", 3.61, 24.05, -118.75),
  RIGHT_ROCKET_FAR_DRIVER_SWITCH: new Location("RIGHT_ROCKET_FAR_DRIVER_SWITCH", 23.39, 24.05, -61.25),

  // Cargo ship
  CARGO_SHIP_LEFT_CLOSE: new Location("CARGO_SHIP_LEFT_CLOSE", 11 + (7.25 / 12.0) - (5.125 / 12.0) - LENGTH / 2.0, 25 + (7.5 / 12.0), 0),
  CARGO_SHIP_LEFT_MIDDLE: new Location("CARGO_SHIP_LEFT_MIDDLE", 11 + (7.25 / 12.0) - (5.125 / 12.0) - LENGTH / 2.0, 23 + (7.375 / 12.0), 0),
  CARGO_SHIP_LEFT_FAR: new Location("CARGO_SHIP_LEFT_FAR", 11 + (7.25 / 12.0) - (5.125 / 12.0) - LENGTH / 2.0, 21 + (11.25 / 12.0), 0),
  CARGO_SHIP_RIGHT_CLOSE: new Location("CARGO_SHIP_RIGHT_CLOSE", 15 + (4.75 / 12.0) + (5.125 / 12.0) + LENGTH / 2.0, 25 + (7.5 / 12.0), 180),
  CARGO_SHIP_RIGHT_MIDDLE: new Location("CARGO_SHIP_RIGHT_MIDDLE", 15 + (4.75 / 12.0) + (5.125 / 12.0) + LENGTH / 2.0, 23 + (7.375 / 12.0), 180),
  CARGO_SHIP_RIGHT_FAR: new Location("CARGO_SHIP_RIGHT_FAR", 15 + (4.75 / 12.0) + (5.125 / 12.0) + LENGTH / 2.0, 21 + (11.25 / 12.0), 180),
  CARGO_SHIP_MIDDLE_LEFT: new Location("CARGO_SHIP_MIDDLE_LEFT", 12 + (7 / 12.0), 18 + (10.875 / 12.0) - (7.5 / 12.0) - LENGTH / 2.0, 90),
  CARGO_SHIP_MIDDLE_RIGHT: new Location("CARGO_SHIP_MIDDLE_RIGHT", 14 + (5 / 12.0), 18 + (10.875 / 12.0) - (7.5 / 12.0) - LENGTH / 2.0, 90),

  // Loading stations
  LEFT_LOADING_STATION: new Location("LEFT_LOADING_STATION", 1 + (10.75 / 12.0), LENGTH / 2.0, -90),
  RIGHT_LOADING_STATION: new Location("RIGHT_LOADING_STATION", 25 + (1.25 / 12.0), LENGTH / 2.0, -90),

  // HAB
  LEVEL_1_LEFT: new Location("LEVEL_1_LEFT", 9 + (8 / 12.0), 4 + LENGTH / 2.0, 90),
  LEVEL_1_MIDDLE: new Location("LEVEL_1_MIDDLE", 13 + (6 / 12.0), 4 + LENGTH / 2.0, 90),
  LEVEL_1_RIGHT: new Location("LEVEL_1_RIGHT", 17 + (4 / 12.0), 4 + LENGTH / 2.0, 90),
  LEVEL_2_LEFT: new Location("LEVEL_2_LEFT", 9 + (8 / 12.0), LENGTH / 2.0, 90),
  LEVEL_2_RIGHT: new Location("LEVEL_2_RIGHT", 17 + (4 / 12.0), LENGTH / 2.0, 90)
}

const SNAP_HEADINGS = [0, 45, 61.25, 90, 118.75, 135, 180, -45, -61.25, -90, -118.75, -135, -180];

function getLocation(key) {
  for (let value in LOCATION) {
    if (value == key)
      return LOCATION[value];
  }

  throw "Location of key \'" + key + "\', not found";
}


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
        if(mouseButton === LEFT) {
            let poseSnapped = false;
            let mousePosition = new Vector2(mouseX, mouseY);
            let heading;
            for (let key in LOCATION) {
              let location = getLocation(key);
              if(Vector2.distance(new Vector2(location.x, 27 - location.y), new Vector2(mousePosition.x / PIXELS_PER_FOOT, mousePosition.y / PIXELS_PER_FOOT)) < SNAP_DISTANCE) {
                poseSnapped = true;
                mousePosition = new Vector2(location.x * PIXELS_PER_FOOT, (27 - location.y) * PIXELS_PER_FOOT);
                heading = location.heading;
              }
            }

            poseDragging.position = mousePosition;
            if (poseSnapped)
            {
              poseDragging.heading = heading;
            }
        }
        else if(mouseButton === RIGHT) {
            let heading = -atan2(mouseY - poseDragging.position.y, mouseX - poseDragging.position.x);
            for (let i in SNAP_HEADINGS) {
              let snapHeading = SNAP_HEADINGS[i];
              if(Math.abs(heading - snapHeading) < SNAP_HEADING) {
                heading = snapHeading;
                break;
              }
            }

            poseDragging.heading = heading;
        }

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

    // Draw locations
    strokeWeight(0);
    fill(255, 0, 0);

    for (let key in LOCATION) {
      getLocation(key).show();
    }

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
