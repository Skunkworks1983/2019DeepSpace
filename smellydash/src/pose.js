class Pose {
  constructor(x, y, heading) {
    this.pos = createVector(x, y);
    this.heading = heading;
  }

  toString() {
    return (precise(this.pos.x) / PIXELS_PER_FOOT) + ", " +
           (precise(this.pos.y) / PIXELS_PER_FOOT) + ", " +
           heading;
  }

  show() {
    push();

    // Draw circle
    translate(this.pos.x, this.pos.y);
    rotate(-(this.heading + 90));
    circle(0, 0, 10);

    // Draw heading
    fill(0);
    triangle(0, 10, -3, 7, 3, 7);

    pop();
  }
}
