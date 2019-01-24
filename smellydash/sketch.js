const remote = require('electron').remote;
const con = remote.getGlobal('console');
con.log("sketch.js got global console");
const ntClient = remote.getGlobal('ntClient');

var img;
var points = [];

var x = 0;
var y = 0;
var angle = 90;

const CANVAS_WIDTH = 590;
const CANVAS_HEIGHT = 555;
const PIXELS_PER_FOOT = CANVAS_HEIGHT / 27;

const ROBOT_WIDTH = (32 / 12) * PIXELS_PER_FOOT;
const ROBOT_HEIGHT = (37 / 12) * PIXELS_PER_FOOT;

var wasConnected = true;
var wasPressed = false;

function precise(num) {
  return Number.parseFloat(num).toFixed(2);
}

function debounceMouse() {
  if(!wasPressed && mouseIsPressed) {
    wasPressed = true;

    if(0 <= mouseX && mouseX <= CANVAS_WIDTH && 0 <= mouseY && mouseY <= CANVAS_HEIGHT) {
      return true
    }
  }
  else if(!mouseIsPressed) {
    wasPressed = false;
  }
  return false;
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

function preload() {
  con.log("preloading field image");
  img = loadImage('cropped_field.png');

  notconnected();
  ntConnect();

  document.getElementById("retryconnect").onclick = ntConnect;
  document.getElementById("sendpath").onclick = function () {
    var pathString = x + "," + y;

    for(var i = 0; i < points.length; i++) {
      pathString = pathString + "," + precise(points[i][0] / PIXELS_PER_FOOT) + "," +
        precise((CANVAS_HEIGHT - points[i][1]) / PIXELS_PER_FOOT);
    }
    con.log(pathString);
  };
  document.getElementById("clearpath").onclick = function () {
    points = [];

    document.getElementById("pathbuttons").style.display = "none";
  };
  document.getElementById("pathbuttons").style.display = "none";
}

function setup() {
  con.log("setup");
  createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
  img.resize(0, CANVAS_HEIGHT);
  angleMode(DEGREES);
  frameRate(50);
  con.log("setup completed");
}

function draw() {
  // con.log("draw");
  if(ntClient.isConnected()){
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
  image(img, 0, 0);

  if(debounceMouse()) {
    if(points.length === 0) {
      document.getElementById("pathbuttons").style.display = "inline-block";
    }
    points[points.length] = [mouseX, mouseY];
  }

  fill(0, 0, 255);

  for(var i = 0; i < points.length; i++) {
    ellipse(points[i][0], points[i][1], 5);
  }

  fill(255, 0, 0);
  translate(x * PIXELS_PER_FOOT, CANVAS_HEIGHT - (y * PIXELS_PER_FOOT));
  rotate(-angle + 90);
  rect(-ROBOT_WIDTH / 2, -ROBOT_HEIGHT / 2, ROBOT_WIDTH, ROBOT_HEIGHT);
}
