const remote = require('electron').remote;
const con = remote.getGlobal('console');
con.log("Sketch.js got global console. All logs are now from here.");
const ntClient = remote.getGlobal('ntClient');

var img;

const CANVAS_WIDTH = 590;
const CANVAS_HEIGHT = 555;
const PIXELS_PER_FOOT = 2;

function preload() {
  con.log("preloading field image");
  img = loadImage('cropped_field.png');
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
  var x = ntClient.getEntry(ntClient.getKeyID("robotX"));
  var y = ntClient.getEntry(ntClient.getKeyID("robotY"));
  var angle = ntClient.getEntry(ntClient.getKeyID("robotAngle"));
  //con.log("x: " + x + ", y: " + y + " angle: " + angle);

  clear();
  image(img, 0, 0);
  fill(255, 0, 0);
  translate(x * PIXELS_PER_FOOT, CANVAS_HEIGHT - (y * PIXELS_PER_FOOT));
  rotate(angle);
  rect(-30, -30, 60, 60);
}
