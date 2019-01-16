package frc.team1983.utilities.control;

import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class PurePursuitController
{
    public static final double LOOK_AHEAD_DISTANCE = 3.5; // Feet

    public static double[] evaluateOutput(Pose pose, Path path, double velocity)
    {
        Vector2 position = pose.getPosition();
        double heading = pose.getHeading();
        double robotT = path.evaluateClosestT(pose.getPosition()); // Get the t value of how far the robot is in the path

        double length = path.getLength();
        double t = robotT + LOOK_AHEAD_DISTANCE / length;

        Vector2 point;
        if (t > 1.0)
        {
            Vector2 end = path.evaluate(1.0);
            Vector2 tangent = path.evaluateTangent(1.0);
            double distToEnd = Vector2.getDistance(position, end);
            double scalar;
            if (robotT < 1.0) scalar = LOOK_AHEAD_DISTANCE - distToEnd;
            else scalar = LOOK_AHEAD_DISTANCE + distToEnd;
            point = Vector2.add(end, Vector2.scale(tangent, scalar));
        }
        else
        {
            point = path.evaluate(t);
        }

        // Get radius of curvature, r, between robot and the look ahead point
        double r;
        double dx = point.getX() - position.getX();
        double dy = point.getY() - position.getY();
        double dist = point.getDistanceTo(position);

        if (dy == 0) r = dist / 2.0 * Math.signum(dx);
        else if (dx == 0) r = 0;
        else
        {
            double dtheta = (Math.atan(dx / dy) - heading) * Math.signum(dy);

            // Law of sines
            r = (dist * Math.sin(Math.PI / 2 - dtheta)) / (Math.sin(2 * dtheta));
        }

        double v = velocity;
        double vl = v, vr = v;

        if(r != 0)
        {
            vl = (v * (r + Drivebase.TRACK_WIDTH / 2.0)) / r;
            vr = (v * (r - Drivebase.TRACK_WIDTH / 2.0)) / r;
        }

        return new double[] {vl / Drivebase.MAX_VELOCITY, vr / Drivebase.MAX_VELOCITY};
    }
}