class Path {
  static show(poses) {
    for(let i = 0; i < poses.length - 1; i++) {
        pose = poses[i];
        nextPose = poses[i + 1];

        var x1 = pose.pos.x;
        var y1 = pose.pos.y;
        var heading1 = -pose.heading;

        var x2 = nextPose.pos.x;
        var y2 = nextPose.pos.y;
        var heading2 = -nextPose.heading;

        bezier(
            x1, y1,
            x1 + cos(angle1) * LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT, y1 + sin(angle1) * LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT,
            x2 + cos(angle2) * -LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT, y2 + sin(angle2) * -LOOKAHEAD_DISTANCE * PIXELS_PER_FOOT,
            x2, y2
        );
    }
  }
}
