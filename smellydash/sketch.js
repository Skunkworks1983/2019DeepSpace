const remote = require('electron').remote;
const con = remote.getGlobal('console');
con.log("sketch.js got global console");
const ntClient = remote.getGlobal('ntClient');

//-------- Variable Init -------------
var img; //Field image
var poses = []; //Pathing poses

//Poses is a 2d array, with the second dimension being x,y,angle
const xIndex = 0; //x is in pixels
const yIndex = 1; //y is in pixels
const angleIndex = 2; //angle is irl gyro heading

//Default starting pose of robot. These values are in feet and irl gyro heading
var x = 5;
var y = 5;
var angle = 45;

var wasConnected = true; //true so it will trigger the reconnect
var wasPressed = false;

const CANVAS_WIDTH = 590;
const CANVAS_HEIGHT = 555;
const PIXELS_PER_FOOT = CANVAS_HEIGHT / 27;

const LOOKAHEAD_DISTANCE = 3.5;

//This is breakout, although it is only a visual thing
const ROBOT_WIDTH = (32 / 12) * PIXELS_PER_FOOT;
const ROBOT_HEIGHT = (37 / 12) * PIXELS_PER_FOOT;

//-------- Misc Functions -------------

//round() is a p5 function, and rounds to integer, which is not ideal
function precise(num) {
  return Number.parseFloat(num).toFixed(2);
}

// Only register mouse press on first frame it is pressed
function debounceMouse() {
  if(!wasPressed && mouseIsPressed) {
    wasPressed = true;

    if(mouseIsInCanvas()) {
      return true
    }
  }
  else if(!mouseIsPressed) {
    wasPressed = false;
  }
  return false;
}

//Detects if the mouse is currently in the canvas, to prevent triggering pathing
// functions when pressing buttons
function mouseIsInCanvas() {
  return 0 <= mouseX && mouseX < CANVAS_WIDTH && 0 <= mouseY && mouseY < CANVAS_HEIGHT
}

// Trys to connect to the network tables
function ntConnect() {
  con.log("trying to connect");

  // Connects the client to the server on team 1983's roborio
  // We do this as early as possible so it has time to connect while the
  // rendering process loads
  ntClient.start((isConnected, err) => {
      // Displays the error and the state of connection
      con.log({ isConnected, err });
  }, '10.19.83.2');
}

//Called when we successfully connect to the network table. Updates buttons
function connected () {
  if(!wasConnected)
  {
    con.log("connected!");
    document.getElementById("retryconnect").style.display = "none";
    document.getElementById("connectionstatus").style.backgroundColor = "green";
    wasConnected = true;
  }
}

//Called when we disconnect to the network table. Updates buttons
function notconnected() {
  if(wasConnected)
  {
    con.log("disconnected :(")
    document.getElementById("retryconnect").style.display = "inline-block";
    document.getElementById("connectionstatus").style.backgroundColor = "red";
    document.getElementById("coords").textContent = " ";
    wasConnected = false;
  }
}

//--------------- preload ----------------

//Called before window even starts rendering
function preload() {
  con.log("preload...");
  img = loadImage('cropped_field.png');

  notconnected();
  ntConnect();

  document.getElementById("retryconnect").onclick = ntConnect;
  document.getElementById("sendpath").onclick = function () {
    con.log("sending a path");
    var pathString = precise(poses[0][0] / PIXELS_PER_FOOT) + "," + precise(poses[0][1] / PIXELS_PER_FOOT) + "," + precise(poses[0][2]);

    for(var i = 1; i < poses.length; i++) {
      pathString = pathString + ":" + precise(poses[i][0] / PIXELS_PER_FOOT) + "," +
        precise((CANVAS_HEIGHT - poses[i][1]) / PIXELS_PER_FOOT) + "," + precise(poses[i][2]);
    }

    ntClient.Update(ntClient.getKeyID("/SmartDashboard/path"), pathString);
    ntClient.Update(ntClient.getKeyID("/SmartDashboard/gotPath"), "false");
    con.log(pathString);
  };
  document.getElementById("clearpath").onclick = function () {
    poses = [];

    document.getElementById("pathbuttons").style.display = "none";
  };
  document.getElementById("removepose").onclick = function () {
    poses.splice(-1); //Delete last element

    if(poses.length === 0) {
        document.getElementById("pathbuttons").style.display = "none";
    }
  }

  document.getElementById("pathbuttons").style.display = "none";

  con.log("preload complete")
}

//------------ setup -----------------

//Called when window starts rendering
function setup() {
  con.log("setup...");
  createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
  img.resize(0, CANVAS_HEIGHT);
  angleMode(DEGREES);
  frameRate(50);
  con.log("setup complete");
}

//Called every frame
function draw() {

  //------- nt getting and coord updating --------
  // con.log("draw");
  if(ntClient.isConnected() && ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotX")) != undefined){
    connected();
    x = precise(ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotX")).val);
    y = precise(ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotY")).val);
    angle = precise(ntClient.getEntry(ntClient.getKeyID("/SmartDashboard/robotAngle")).val);
    document.getElementById("coords").textContent = "X: " + x +
      " Y: " + y + " \u0398: " + angle;
    // con.log("x: " + x + ", y: " + y + " angle: " + angle);
  }
  else{
    notconnected();
  }

  clear();
  rectMode(CENTER);
  image(img, 0, 0);

  debounced = debounceMouse();

  //Create pose
  if(debounced) {
    if(poses.length === 0) {
      document.getElementById("pathbuttons").style.display = "inline-block";
    }
    poses[poses.length] = [mouseX, mouseY, 0];
  }
  //Rotate pose
  if(!debounced && mouseIsPressed && mouseIsInCanvas()) {
    poses[poses.length - 1][2] = -atan2(mouseY - poses[poses.length - 1][1],
      mouseX - poses[poses.length - 1][0]);
    // con.log(poses[poses.length - 1][2]);
    fill(0, 255, 0)
    ellipse(mouseX, mouseY, 5);
  }

  //Render poses
  for(var i = 0; i < poses.length; i++) {
    push();
    rectMode(CENTER);
    fill(0, 0, 255);
    translate(poses[i][0], poses[i][1]);
    rotate(-1 * (poses[i][2] + 90));
    circle(0, 0, 10);
    fill(0, 0, 0);
    triangle(0, 10, -3, 7, 3, 7);
    translate(-poses[i][0], -poses[i][1]);
    rotate(poses[i][2] + 90);
    pop();
  }

  //Draw path
  fill(0, 0);
  stroke(0, 0, 255);
  strokeWeight(2);
  for(let i = 0; i < poses.length - 1; i++) {
      pose = poses[i];
      nextpose = poses[i + 1]

      angle1 = -pose[angleIndex];
      angle2 = -nextpose[angleIndex];

      bezier(
          pose[xIndex], pose[yIndex],
          pose[xIndex] + cos(angle1) * LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT, pose[yIndex] + sin(angle1) * LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT,
          nextpose[xIndex] + cos(angle2) * -LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT, nextpose[yIndex] + sin(angle2) * -LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT,
          nextpose[xIndex], nextpose[yIndex]
      );
  }
  stroke(0);
  strokeWeight(1);

  //Draw robot
  rectMode(CENTER);
  fill(255, 0, 0);
  translate(x * PIXELS_PER_FOOT, CANVAS_HEIGHT - (y * PIXELS_PER_FOOT));
  angleMode(DEGREES);
  rotate(-angle - 90);
  rect(0, 0, ROBOT_WIDTH, ROBOT_HEIGHT);

  //Draw heading
  fill(0, 0, 0);
  triangle(0, ROBOT_HEIGHT / 2, -3, ROBOT_HEIGHT / 2 - 3, 3, ROBOT_HEIGHT / 2 - 3)
}
