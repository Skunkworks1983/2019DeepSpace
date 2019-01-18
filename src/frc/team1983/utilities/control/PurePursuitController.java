package frc.team1983.utilities.control;

import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Line;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class PurePursuitController
{
    public static final double LOOK_AHEAD_DISTANCE = 3.5; // Feet
    public static final double SLOWDOWN_DISTANCE = 3;

    // index 0 in returned array is left output, 1 is right
    public static double[] evaluateOutput(Pose pose, Path path, double velocity)
    {
        double[] output = new double[] {velocity / Drivebase.MAX_VELOCITY, velocity / Drivebase.MAX_VELOCITY};

        // find closest point on path to robot
        Pair closest = path.evaluateClosestPoint(pose.getPosition());
        double closestT = (double) closest.getValue1();
        Vector2 closestPoint = (Vector2) closest.getValue2();

        // find lookAhead point
        Vector2 lookAhead;
        double t = closestT + LOOK_AHEAD_DISTANCE / path.getLength();
        if (t > 1.0)
        {
            Vector2 end = path.evaluate(1.0);
            Vector2 tangent = path.evaluateTangent(1.0);
            double distToEnd = Vector2.getDistance(pose.getPosition(), end);
            double scalar;
            if (closestT < 1.0) scalar = LOOK_AHEAD_DISTANCE - distToEnd;
            else scalar = LOOK_AHEAD_DISTANCE + distToEnd;
            lookAhead = Vector2.add(end, Vector2.scale(tangent, scalar));
        }
        else
        {
            lookAhead = path.evaluate(t);
        }

        // find icc
        Vector2 icc = Line.cast(
                new Line(pose.getPosition(), Vector2.rotate(pose.getDirection(), 90.0)),
                new Line(Vector2.findCenter(pose.getPosition(), lookAhead),
                         Vector2.rotate(Vector2.sub(lookAhead, pose.getPosition()).getNormalized(), 90.0))
        );

        if(icc == null)
            return output;

        // calculate radius of curvature
        double radius = Vector2.getDistance(pose.getPosition(), icc);
        double direction = Vector2.dot(Vector2.rotate(pose.getDirection(), 90.0),
                                       Vector2.sub(icc, pose.getPosition()).getNormalized());
        radius *= Math.signum(direction);


        // calculate output
        double distanceToEnd = path.getLength() * (1 - closestT);
        velocity = distanceToEnd < SLOWDOWN_DISTANCE ? velocity * distanceToEnd / SLOWDOWN_DISTANCE : velocity;

        output[0] = velocity * (radius - Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY;
        output[1] = velocity * (radius + Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY;

        return output;
    }
}