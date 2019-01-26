package frc.team1983.utilities.control;

import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Line;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

/**
 * PurePursuitController determines motor outputs from the current position, a path to follow, and following velocity.
 * Positive radius of curvature is to the right
 * Negative radius of curvature is to the left
 */
public class PurePursuitController
{
    public static final double LOOK_AHEAD_DISTANCE = 3.5; // Feet
    public static final double SLOWDOWN_DISTANCE = 1;

    // value1 in returned array is left output, value2 is right
    public static Pair evaluateOutput(Pose pose, Path path, double velocity)
    {
        Pair output = new Pair(velocity / Drivebase.MAX_VELOCITY, velocity / Drivebase.MAX_VELOCITY);

        if(velocity < 0)
            pose = new Pose(pose.getPosition(), pose.getDirection().getNegative());

        Pair lookAhead = evaluateLookAheadPoint(pose, path);
        double lookAheadT = (double) lookAhead.getValue1();
        Vector2 lookAheadPoint = (Vector2) lookAhead.getValue2();
        Vector2 icc = evaluateCenterOfCurvature(pose, lookAheadPoint);

        if(icc == null)
            return output;

        double radius = evaluateRadiusOfCurvature(pose, icc);
        double distanceToEnd = evaluateDistanceToEnd(pose, path, lookAheadT);

        velocity = distanceToEnd < SLOWDOWN_DISTANCE ? velocity * (distanceToEnd / SLOWDOWN_DISTANCE) : velocity;
//        velocity = distanceToEnd < SLOWDOWN_DISTANCE ? 0 : velocity;

        output.setValue1(velocity * (radius + Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY);
        output.setValue2(velocity * (radius - Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY);

        return output;
    }

    protected static Pair evaluateLookAheadPoint(Pose pose, Path path)
    {
        // find closest point on path to robot
        Pair closest = path.evaluateClosestPoint(pose.getPosition());
        double closestT = (double) closest.getValue1();

        // find lookAhead point
        double lookAheadT = closestT + LOOK_AHEAD_DISTANCE / path.getLength();
        Vector2 lookAhead;

        // if look ahead is outside of path bounds, evaluate along continuing tangent
        if(lookAheadT > 1.0)
            lookAhead = Vector2.add(path.evaluate(1.0), Vector2.scale(path.evaluateTangent(1.0), (lookAheadT - 1.0) * path.getLength()));
        else
            lookAhead = path.evaluate(lookAheadT);

        return new Pair(lookAheadT, lookAhead);
    }

    protected static Vector2 evaluateCenterOfCurvature(Pose pose, Vector2 lookAhead)
    {
        return Line.cast(
                new Line(pose.getPosition(), Vector2.rotate(pose.getDirection(), 90.0)),
                new Line(Vector2.findCenter(pose.getPosition(), lookAhead),
                         Vector2.rotate(Vector2.sub(lookAhead, pose.getPosition()).getNormalized(), 90.0))
        );
    }

    protected static double evaluateRadiusOfCurvature(Pose pose, Vector2 icc)
    {
        double radius = Vector2.getDistance(pose.getPosition(), icc);
        double direction = Vector2.dot(Vector2.rotate(pose.getDirection(), -90.0), Vector2.sub(icc, pose.getPosition()).getNormalized());
        radius *= Math.signum(direction);

        return radius;
    }

    protected static double evaluateDistanceToEnd(Pose pose, Path path, double lookAheadT)
    {
        return (lookAheadT >= 1.0) ? 0.0 : Vector2.getDistance(pose.getPosition(), path.evaluate(1.0));
    }
}