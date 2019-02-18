class Location {
  constructor(key, x, y, heading) {
    this.key = key;
    this.x = x;
    this.y = y;
    this.heading = heading;
  }

  show() {
    let size = 0.75;
    let lineWeight = 0.1;

    push();

    scale(PIXELS_PER_FOOT, -PIXELS_PER_FOOT);
    translate(this.x, this.y - 27);

    ellipse(0, 0, size);

    strokeWeight(0.1);
    rotate(this.heading);
    line(0, 0, size, 0);

    strokeWeight(0);

    pop();
  }
}

module.exports = Location;
