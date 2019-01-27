package frc.team1983.utilities.control;

import frc.team1983.Robot;
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
    public static final double LOOKAHEAD_DISTANCE = 3.5; // feet

    /**
     * Evaluates the motor output
     * @param pose position of the robot
     * @param path path to follow
     * @param velocity the velocity to follow the path at
     * @return motor velocities, value1 is left, value2 is right
     */
    public static Pair evaluateOutput(Pose pose, Path path, double velocity)
    {
        Pair output = new Pair(velocity / Drivebase.MAX_VELOCITY, velocity / Drivebase.MAX_VELOCITY);

        if(velocity < 0)
            pose = new Pose(pose.getPosition(), pose.getDirection().getNegative());

        Vector2 lookahead = evaluateLookaheadPoint(pose, path);
        Vector2 icc = evaluateCenterOfCurvature(pose, lookahead);

        if(icc == null)
            return output;

        double radius = evaluateRadiusOfCurvature(pose, icc);

        double distanceToEnd = Vector2.getDistance(pose.getPosition(), path.evaluate(1.0));

        boolean pastPath = (distanceToEnd < LOOKAHEAD_DISTANCE) &&
                            Vector2.dot(path.evaluateTangent(1.0), Vector2.sub(pose.getPosition(), path.evaluate(1.0)).getNormalized()) > 0;

        velocity *= (pastPath ? -1 : 1) * Math.min(distanceToEnd / LOOKAHEAD_DISTANCE, 1);

        output.setValue1(velocity * (radius + Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY);
        output.setValue2(velocity * (radius - Drivebase.TRACK_WIDTH / 2.0) / radius / Drivebase.MAX_VELOCITY);

        return output;
    }

    /**
     * Evaluate a point ahead of the robot follow
     * @param pose position of the robot
     * @param path path to follow
     * @return look ahead point
     */
    protected static Vector2 evaluateLookaheadPoint(Pose pose, Path path)
    {
        // find closest point on path to robot
        Pair closest = path.evaluateClosestPoint(pose.getPosition());
        double closestT = (double) closest.getValue1();

        // find lookAhead point
        double lookaheadT = closestT + LOOKAHEAD_DISTANCE / path.getLength();

        // if look ahead is outside of path bounds, evaluate along continuing tangent
        Vector2 lookahead = path.evaluate(lookaheadT);
        if(lookaheadT > 1.0)
            lookahead = Vector2.add(path.evaluate(1.0), Vector2.scale(path.evaluateTangent(1.0), (lookaheadT - 1.0) * path.getLength()));

        return lookahead;
    }

    /**
     * Evaluates a point the robot needs to rotate about in order to reach the look ahead point
     * @param pose position of the robot
     * @param lookahead look ahead point for the robot to target
     * @return center of curvature
     */
    protected static Vector2 evaluateCenterOfCurvature(Pose pose, Vector2 lookahead)
    {
        return Line.cast(
                new Line(pose.getPosition(), Vector2.rotate(pose.getDirection(), 90.0)),
                new Line(Vector2.findCenter(pose.getPosition(), lookahead),
                         Vector2.rotate(Vector2.sub(lookahead, pose.getPosition()).getNormalized(), 90.0))
        );
    }

    /**
     * Evaluates the radius that the center of curvature is from the robot
     * Positive radius of curvature is to the right
     * Negative radius of curvature is to the left
     * @param pose position of the robot
     * @param icc center of curvature
     * @return radius of curvature
     */
    protected static double evaluateRadiusOfCurvature(Pose pose, Vector2 icc)
    {
        double radius = Vector2.getDistance(pose.getPosition(), icc);
        double direction = Vector2.dot(Vector2.rotate(pose.getDirection(), -90.0), Vector2.sub(icc, pose.getPosition()).getNormalized());
        radius *= Math.signum(direction);

        return radius;
    }
}