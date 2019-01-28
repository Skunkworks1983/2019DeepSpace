// ---------- Constants ----------

// Default starting pose of robot. These values are in feet and gyro heading
const START_POS_X = 5;
const START_POS_Y = 5;
const START_HEADING = 45;

const CANVAS_WIDTH = 590;
const CANVAS_HEIGHT = 555;
const PIXELS_PER_FOOT = CANVAS_HEIGHT / 27;

const LOOKAHEAD_DISTANCE = 3.5;

// This is breakout, although it is only a visual thing
const ROBOT_WIDTH = (32 / 12) * PIXELS_PER_FOOT;
const ROBOT_HEIGHT = (37 / 12) * PIXELS_PER_FOOT;

// ---------- Functions ----------

// round() is a p5 function, and rounds to integer, which is not ideal
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
