const remote = require('electron').remote;
const con = remote.getGlobal('console');
con.log("sketch.js got global console");
const ntClient = remote.getGlobal('ntClient');

//-------- Variable Init -------------
var img;
var poses = [];

var x = 5;
var y = 5;
var angle = 45;

var wasConnected = true; //true so it will trigger the reconnect
var wasPressed = false;

const CANVAS_WIDTH = 590;
const CANVAS_HEIGHT = 555;
const PIXELS_PER_FOOT = CANVAS_HEIGHT / 27;

const ROBOT_WIDTH = (32 / 12) * PIXELS_PER_FOOT;
const ROBOT_HEIGHT = (37 / 12) * PIXELS_PER_FOOT;

//-------- Misc Functions -------------

function precise(num) {
  return Number.parseFloat(num).toFixed(2);
}

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

function mouseIsInCanvas() {
  return 0 <= mouseX && mouseX <= CANVAS_WIDTH && 0 <= mouseY && mouseY <= CANVAS_HEIGHT
}

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

function connected () {
  if(!wasConnected)
  {
    con.log("connected!");
    document.getElementById("retryconnect").style.display = "none";
    document.getElementById("connectionstatus").style.backgroundColor = "green";
    wasConnected = true;
  }
}

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

function preload() {
  con.log("preload...");
  img = loadImage('cropped_field.png');

  notconnected();
  ntConnect();

  document.getElementById("retryconnect").onclick = ntConnect;
  document.getElementById("sendpath").onclick = function () {
    con.log("sending a path");
    var pathString = x + "," + y + "," + angle;

    for(var i = 0; i < poses.length; i++) {
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

function setup() {
  con.log("setup...");
  createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
  img.resize(0, CANVAS_HEIGHT);
  angleMode(DEGREES);
  frameRate(50);
  con.log("setup complete");
}

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

  if(debounced) {
    if(poses.length === 0) {
      document.getElementById("pathbuttons").style.display = "inline-block";
    }
    poses[poses.length] = [mouseX, mouseY, 0];
  }
  if(!debounced && mouseIsPressed && mouseIsInCanvas()) {
    poses[poses.length - 1][2] = -atan2(mouseY - poses[poses.length - 1][1],
      mouseX - poses[poses.length - 1][0]);
    // con.log(poses[poses.length - 1][2]);
    fill(0, 255, 0)
    ellipse(mouseX, mouseY, 5);
  }

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

  rectMode(CENTER);
  fill(255, 0, 0);
  translate(x * PIXELS_PER_FOOT, CANVAS_HEIGHT - (y * PIXELS_PER_FOOT));
  angleMode(DEGREES);
  rotate(-angle - 90);
  rect(0, 0, ROBOT_WIDTH, ROBOT_HEIGHT);

  fill(0, 0, 0);
  triangle(0, ROBOT_HEIGHT / 2, -3, ROBOT_HEIGHT / 2 - 3, 3, ROBOT_HEIGHT / 2 - 3)
}
