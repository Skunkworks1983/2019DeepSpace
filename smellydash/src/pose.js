class Pose {
    constructor(x, y, heading) {
        this.position = new Vector2(x, y);
        this.heading = heading;
    }

    get forward() {
        return new Vector2(cos(-this.heading), sin(-this.heading));
    }
}

Pose.prototype.toString = function toString() {
    return (this.position.x / PIXELS_PER_FOOT).toFixed(2) + "," +
           (this.position.y / PIXELS_PER_FOOT).toFixed(2) + "," +
           this.heading.toFixed(2);
}

module.exports = Pose;
