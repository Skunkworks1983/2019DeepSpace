package frc.team1983.utilities.control;

import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Line;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

/**
 * Positive radius of curvature is to the right
 * Negative radius of curvature is to the left
 */
public class PurePursuitController
{
    public static final double LOOK_AHEAD_DISTANCE = 3.5; // Feet
    public static final double SLOWDOWN_DISTANCE = 5;
    // value1 in returned array is left output, value2 is right
    public static Pair evaluateOutput(Pose pose, Path path, double velocity)
    {
        Pair output = new Pair(velocity / Drivebase.MAX_VELOCITY, velocity / Drivebase.MAX_VELOCITY);

        if(velocity < 0)
            pose = new Pose(pose.getPosition(), pose.getDirection().getNegative());

        Vector2 lookAhead = evaluateLookAheadPoint(pose, path);
        Vector2 icc = evaluateCenterOfCurvature(pose, lookAhead);

        if(icc == null)
            return output;

        double radius = evaluateRadiusOfCurvature(pose, icc);
        double distanceToEnd = Vector2.getDistance(pose.getPosition(), path.evaluate(1.0));

        velocity = distanceToEnd < SLOWDOWN_DISTANCE ? velocity * distanceToEnd / SLOWDOWN_DISTANCE : velocity;

        output.setValue1(velocity * (radius + Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY);
        output.setValue2(velocity * (radius - Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY);

        return output;
    }

    protected static Vector2 evaluateLookAheadPoint(Pose pose, Path path)
    {
        // find closest point on path to robot
        Pair closest = path.evaluateClosestPoint(pose.getPosition());
        double closestT = (double) closest.getValue1();

        // find lookAhead point
        double lookaheadT = closestT + LOOK_AHEAD_DISTANCE / path.getLength();
        Vector2 lookAhead = path.evaluate(lookaheadT);

        // if look ahead is outside of path bounds, evaluate along continuing tangent
        if(lookaheadT > 1.0)
            lookAhead = Vector2.add(path.evaluate(1.0), Vector2.scale(path.evaluateTangent(1.0), (lookaheadT - 1) * LOOK_AHEAD_DISTANCE));

        return lookAhead;
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
}