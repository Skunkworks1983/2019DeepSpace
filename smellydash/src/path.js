class Path {
  static show(poses) {
    poses.forEach(pose => {
        let index = poses.indexOf(pose);
        if(index < poses.length - 1) {
            let nextPose = poses[index + 1];

            let cp0 = Vector2.scale(pose.forward, TANGENT_LENGTH * PIXELS_PER_FOOT);
            let cp1 = Vector2.scale(nextPose.forward, -TANGENT_LENGTH * PIXELS_PER_FOOT);

            let points = [
                new Vector2(pose.position.x, pose.position.y),
                new Vector2(pose.position.x + cp0.x, pose.position.y + cp0.y),
                new Vector2(nextPose.position.x + cp1.x, nextPose.position.y + cp1.y),
                new Vector2(nextPose.position.x, nextPose.position.y)
            ];

            bezier(
                points[0].x, points[0].y,
                points[1].x, points[1].y,
                points[2].x, points[2].y,
                points[3].x, points[3].y
            );

            // Draw track
            let resolution = 50; // should be constant but test
            for(let offset = -ROBOT_WIDTH/2; offset < ROBOT_WIDTH; offset += ROBOT_WIDTH/2) {
                for(let index = 0; index < resolution; index++) {
                    let t = index / resolution;

                    let p0 = Bezier.offset(index / resolution, offset, points);
                    let p1 = Bezier.offset((index + 1) / resolution, offset, points);

                    line(p0.x, p0.y, p1.x, p1.y);
                }
            }
        }
    });
  }
}

module.exports = Path;
