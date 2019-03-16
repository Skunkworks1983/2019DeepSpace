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
    public static final double STEERING_FACTOR = 1.25; // unitless
    public static final double LOOKAHEAD_DISTANCE = 5.0; // feet
    public static final double SLOWDOWN_DISTANCE = 5.0; // feet
    public static final double SLOWDOWN_FACTOR = 1.0; // unitless
    // TODO: tune
    public static final double CURVATURE_SLOWDOWN = 0.05; // unitless

    public static final double ANGLE_CORRECTION = 0.75; // unitless
    public static final double ANGLE_CORRECTION_DISTANCE = 3.0; // feet
    public static final double MAX_ANGLE_CORRECTION = 0.4;

    public static final double VELOCITY_DEADZONE = 0.3; // feet
    public static final double HEADING_DEADZONE = 12; // degrees

    /**
     * Evaluates the motor output
     *
     * @param pose     position of the robot
     * @param path     path to follow
     * @param velocity the velocity to follow the path at
     * @return motor velocities, value1 is left, value2 is right
     */
    public static Pair<Double, Double> evaluateOutput(Pose pose, Path path, double velocity)
    {
        Pair<Double, Double> output = new Pair<>(velocity, velocity);

        // If deadzoned stop
        if (PurePursuitController.inDeadzone(pose, path))
            return new Pair<>(0.0, 0.0);

//        Vector2 closestPoint = path.evaluateClosestPoint(pose.getPosition());
//        SmartDashboard.putNumber("closestPointX", closestPoint.getX());
//        SmartDashboard.putNumber("closestPointY", closestPoint.getY());

        Vector2 end = path.evaluate(1.0);
        Vector2 endTangent = path.evaluateTangent(1.0);

        if (path.isReversed())
        {
            pose = new Pose(pose.getPosition(), pose.getDirection().getNegative());
            velocity *= -1;
        }

        Vector2 lookahead = evaluateLookaheadPoint(pose, path);

//        SmartDashboard.putNumber("lookaheadX", lookahead.getX());
//        SmartDashboard.putNumber("lookaheadY", lookahead.getY());

        Vector2 icc = evaluateCenterOfCurvature(pose, lookahead);

        double distanceToEnd = Vector2.getDistance(pose.getPosition(), end);

        // Reverse velocity if past end of path
        boolean pastPath = (path.evaluateClosestT(pose.getPosition()) >= 1.0) &&
                Vector2.dot(endTangent, Vector2.sub(pose.getPosition(), end).getNormalized()) > 0;

        velocity *= (pastPath ? -1 : 1) * Math.min(distanceToEnd / SLOWDOWN_DISTANCE / SLOWDOWN_FACTOR, 1);

        // Slow down around curves
        double t = path.evaluateClosestT(pose.getPosition());
        Vector2 curveIcc = path.evaluateCenterOfCurvature(t);
        // TODO: test
//        if (curveIcc != null)
//        {
//            double slowdown = Math.min(CURVATURE_SLOWDOWN / path.evaluateRadiusOfCurvatuve(t), velocity);
//
//            System.out.println(slowdown);
//
//            if (velocity > 0)
//                velocity -= slowdown;
//            else
//                velocity += slowdown;
//        }

        // Correct for angle error
        double angleCorrection = 0;
        if(distanceToEnd < ANGLE_CORRECTION_DISTANCE)
            angleCorrection = Drivebase.MAX_VELOCITY * Math.max(Math.min(getAngleError(endTangent, pose) / 180.0 * ANGLE_CORRECTION, MAX_ANGLE_CORRECTION), -MAX_ANGLE_CORRECTION);

        if(inDistanceDeadzone(pose, path))
            velocity = 0;

        // If there is no center of curvature, go straight with angle correction
        if(icc == null)
            return new Pair<>(velocity - angleCorrection, velocity + angleCorrection);

        double radius = evaluateRadiusOfCurvature(pose, icc) * STEERING_FACTOR;

        // Set velocities
        int sign = path.isReversed() ? -1 : 1;
        output.setValue1(velocity * (radius + sign * Drivebase.TRACK_WIDTH / 2.0) / radius - angleCorrection);
        output.setValue2(velocity * (radius - sign * Drivebase.TRACK_WIDTH / 2.0) / radius + angleCorrection);

        return output;
    }

    /**
     * Evaluate a point ahead of the robot follow
     *
     * @param pose pose of the robot
     * @param path path to follow
     * @return look ahead point
     */
    protected static Vector2 evaluateLookaheadPoint(Pose pose, Path path)
    {
        // Find closest point on path to robot
        double closestT = path.evaluateClosestT(pose.getPosition());

        // Find lookAhead point
        double lookaheadT = closestT + LOOKAHEAD_DISTANCE / path.getLength();

        // If look ahead is outside of path bounds, evaluate along continuing tangent
        Vector2 lookahead;
        if (lookaheadT > 1.0)
            lookahead = Vector2.add(path.evaluate(1.0), Vector2.scale(path.evaluateTangent(1.0), (lookaheadT - 1.0) * path.getLength()));
        else
            lookahead = path.evaluate(lookaheadT);

        return lookahead;
    }

    /**
     * Evaluates a point the robot needs to rotate about in order to reach the look ahead point
     *
     * @param pose      pose of the robot
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
     *
     * @param pose pose of the robot
     * @param icc  center of curvature
     * @return radius of curvature
     */
    protected static double evaluateRadiusOfCurvature(Pose pose, Vector2 icc)
    {
        double radius = Vector2.getDistance(pose.getPosition(), icc);
        double direction = Vector2.dot(Vector2.rotate(pose.getDirection(), -90.0), Vector2.sub(icc, pose.getPosition()).getNormalized());
        radius *= Math.signum(direction);

        return radius;
    }

    /**
     * Find the angle error, between [-180, 180], from an angle and a pose
     *
     * @param target target angle
     * @param pose   current pose
     * @return error in degrees
     */
    protected static double getAngleError(double target, Pose pose)
    {
        // Map target from [0, 360]
        target %= 360;
        if (target < 0) target += 360;

        // Find heading [0, 360]
        double heading = pose.getHeading();
        if (heading < 0) heading += 360;

        // Find error and angle correction
        double error = (target - heading);
        if (error >= 180) error -= 360;
        else if (error <= -180) error += 360;

        return error;
    }

    /**
     * Find the angle error, between [-180, 180], from a direction and a pose
     *
     * @param direction target direction
     * @param pose      current pose
     * @return error in degrees
     */
    protected static double getAngleError(Vector2 direction, Pose pose)
    {
        return getAngleError(Math.toDegrees(Math.atan2(direction.getY(), direction.getX())), pose);
    }

    /**
     * Determine if robot is within the distance deadzone
     *
     * @param pose pose of the robot
     * @param path path to follow
     * @return if in distance deadzone
     */
    public static boolean inDistanceDeadzone(Pose pose, Path path)
    {
        Vector2 end = path.evaluate(1.0);
        Vector2 endTangent = path.evaluateTangent(1.0);

        double deadzoneT = VELOCITY_DEADZONE / path.getLength();

        Vector2 pointAhead = Vector2.add(end, Vector2.scale(endTangent, deadzoneT * path.getLength()));
        Vector2 pointBehind = Vector2.add(end, Vector2.scale(endTangent, -deadzoneT * path.getLength()));
        Vector2 pointClosest = Line.closest(new Line(end, endTangent), pose.getPosition());

        double closeToEnd = path.evaluateClosestT(pose.getPosition());

        return closeToEnd > 1.0 - deadzoneT * 2
                && Vector2.getDistance(pointBehind, pointClosest) < VELOCITY_DEADZONE * 2
                && Vector2.getDistance(pointAhead, pointClosest) < VELOCITY_DEADZONE * 2;
    }

    /**
     * Determine if robot is within the heading deadzone
     *
     * @param pose pose of the robot
     * @param path path to follow
     * @return if in heading deadzone
     */
    public static boolean inHeadingDeadzone(Pose pose, Path path)
    {
        Vector2 endTangent = path.evaluateTangent(1.0);

        Vector2 direction = path.isReversed() ? endTangent.getNegative() : endTangent.copy();
        double angleError = getAngleError(direction, pose);
        System.out.println(angleError);
        return ((angleError >= 0 && angleError < HEADING_DEADZONE) || (angleError <= 0 && angleError > -HEADING_DEADZONE));
    }

    /**
     * Determine if the robot is within the distance deadzone and heading deadzone
     *
     * @param pose pose of the robot
     * @param path path to follow
     * @return in deadzone
     */
    public static boolean inDeadzone(Pose pose, Path path)
    {
        return inDistanceDeadzone(pose, path) && inHeadingDeadzone(pose, path);
    }
}
