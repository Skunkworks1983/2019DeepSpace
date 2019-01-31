class Pose {
    constructor(x, y, heading) {
        this.position = new Vector2(x, y);
        this.heading = heading;
    }

    get forward() {
        return new Vector2(cos(-this.heading), sin(-this.heading));
    }

    show() {
      push();

      translate(this.position.x, this.position.y);
      rotate(-(this.heading + 90));
      rect(0, 0, ROBOT_WIDTH, ROBOT_HEIGHT)

      fill(0);
      triangle(0, 5, 5, 0, -5, 0);

      pop();
    }

    showText() {
      text(this, this.position.x, this.position.y)
    }
}

Pose.prototype.toString = function toString() {
    return (this.position.x / PIXELS_PER_FOOT).toFixed(2) + "," +
           (27 - (this.position.y / PIXELS_PER_FOOT)).toFixed(2) + "," +
           this.heading.toFixed(2);
}

module.exports = Pose;
