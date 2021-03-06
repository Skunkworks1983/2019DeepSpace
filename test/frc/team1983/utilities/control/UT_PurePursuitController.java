package frc.team1983.utilities.control;

import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;

public class UT_PurePursuitController
{
    @Test
    public void outputTest()
    {
        Pose pose = new Pose(0, 5, 90);

        Path path = new Path(
                Pose.ORIGIN,
                new Pose(0, 10, 90)
        );

        Pair output = PurePursuitController.evaluateOutput(pose, path, 1);
        double lv = (double) output.getValue1();
        double rv = (double) output.getValue2();
        assertThat(lv > 0, equalTo(true));
        assertThat(rv > 0, equalTo(true));

        pose = new Pose(0, 15, 90);

        output = PurePursuitController.evaluateOutput(pose, path, 1);
        lv = (double) output.getValue1();
        rv = (double) output.getValue2();
        assertThat(lv < 0, equalTo(true));
        assertThat(rv < 0, equalTo(true));
    }

    @Test
    public void lookaheadTest()
    {
        Pose pose = new Pose(0, 5, 90);

        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        assertThat(Vector2.getDistance(new Vector2(0, 5 + PurePursuitController.LOOKAHEAD_DISTANCE), PurePursuitController.evaluateLookaheadPoint(pose, path)) <= 0.01, equalTo(true));

        pose = new Pose(0, 9, 90);
        assertThat(Vector2.getDistance(new Vector2(0, 9 + PurePursuitController.LOOKAHEAD_DISTANCE), PurePursuitController.evaluateLookaheadPoint(pose, path)) <= 0.01, equalTo(true));

        pose = new Pose(0, 10, 90);
        assertThat(Vector2.getDistance(new Vector2(0, 10 + PurePursuitController.LOOKAHEAD_DISTANCE), PurePursuitController.evaluateLookaheadPoint(pose, path)) <= 0.01, equalTo(true));
    }

    @Test
    public void centerOfCurvatureTest()
    {
        Pose pose = new Pose(0, 0, 90);
        Vector2 lookAhead = new Vector2(0, 10);

        assertThat(PurePursuitController.evaluateCenterOfCurvature(pose, lookAhead), equalTo(null));
    }

    @Test
    public void radiusOfCurvatureTest()
    {
        Vector2 icc = new Vector2(0, 5);

        Pose pose = new Pose(5, 0, 90);
        assertThat(PurePursuitController.evaluateRadiusOfCurvature(pose, icc) < 0, equalTo(true));

        pose = new Pose(-5, 0, 90);
        assertThat(PurePursuitController.evaluateRadiusOfCurvature(pose, icc) > 0, equalTo(true));
    }

    @Test
    public void inDeadzoneTest()
    {
        double VELOCITY_DEADZONE = PurePursuitController.VELOCITY_DEADZONE;
        double HEADING_DEADZONE = PurePursuitController.HEADING_DEADZONE;

        Pose pose = new Pose(0, 10, 90);

        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        pose = new Pose(0, 10 + VELOCITY_DEADZONE - 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        pose = new Pose(0, 10 - VELOCITY_DEADZONE + 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        assertThat(PurePursuitController.evaluateOutput(pose, path, 1).getValue1(), equalTo(0.0));
        assertThat(PurePursuitController.evaluateOutput(pose, path, 1).getValue2(), equalTo(0.0));

        pose = new Pose(0, 10 + VELOCITY_DEADZONE + 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 10 - VELOCITY_DEADZONE - 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 0, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 20, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        assertNotEquals(PurePursuitController.evaluateOutput(pose, path, 1).getValue1(), 0.0);
        assertNotEquals(PurePursuitController.evaluateOutput(pose, path, 1).getValue2(), 0.0);

        pose = new Pose(0, 10, 90 + HEADING_DEADZONE - 1e-3);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        pose = new Pose(0, 10, 90 - HEADING_DEADZONE + 1e-3);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        pose = new Pose(0, 10, 90 + HEADING_DEADZONE + 1e-3);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 10, 90 - HEADING_DEADZONE - 1e-3);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 5, 0);

        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));
        assertNotEquals(PurePursuitController.evaluateOutput(pose, path, 1).getValue1(), 0.0);
        assertNotEquals(PurePursuitController.evaluateOutput(pose, path, 1).getValue2(), 0.0);
    }

    @Test
    public void reverseTest()
    {
        Pose pose = new Pose(0, 0, 90);

        Path path = new Path(
                true,
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        assertThat(PurePursuitController.evaluateOutput(pose, path, 1).getValue1() < 0, equalTo(true));
        assertThat(PurePursuitController.evaluateOutput(pose, path, 1).getValue2() < 0, equalTo(true));
    }

    @Test
    public void pastPathTest()
    {
        Pose pose = new Pose(0, 11, 90);

        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        Pair<Double, Double> output = PurePursuitController.evaluateOutput(pose, path, 1);

        assertThat(output.getValue1() < 0, equalTo(true));
        assertThat(output.getValue2() < 0, equalTo(true));
    }

    @Test
    public void getAngleErrorTest()
    {
        Pose pose = new Pose(10, 10, 10);
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(10, 10, 0)
        );

        Vector2 endTangent = path.evaluateTangent(1.0);
        double angleError = PurePursuitController.getAngleError(endTangent, pose);
        assertThat(angleError, equalTo(-10.0));

        pose = Pose.RIGHT_ROCKET_FAR_DRIVER_SWITCH;
        path = Path.REVERSED_LEVEL_1_RIGHT_TO_ROCKET_FAR_LINE_UP;

        endTangent = path.evaluateTangent(1.0).getNegative();
        angleError = PurePursuitController.getAngleError(endTangent, pose);
        assertThat(angleError, equalTo(0.0));

        pose = new Pose(Pose.RIGHT_ROCKET_FAR_DRIVER_SWITCH.getPosition().getX(),
                        Pose.RIGHT_ROCKET_FAR_DRIVER_SWITCH.getPosition().getY(),
                        Pose.RIGHT_ROCKET_FAR_DRIVER_SWITCH.getHeading() + 10);

        endTangent = path.evaluateTangent(1.0).getNegative();
        angleError = PurePursuitController.getAngleError(endTangent, pose);
        assertThat(angleError, equalTo(-10.0));

        pose = new Pose(Pose.RIGHT_ROCKET_FAR_DRIVER_SWITCH.getPosition().getX(),
                Pose.RIGHT_ROCKET_FAR_DRIVER_SWITCH.getPosition().getY(),
                Pose.RIGHT_ROCKET_FAR_DRIVER_SWITCH.getHeading() - 10);

        endTangent = path.evaluateTangent(1.0).getNegative();
        angleError = PurePursuitController.getAngleError(endTangent, pose);
        assertThat(angleError, equalTo(10.0));

        pose = new Pose(10, 10, -80);
        path = new Path(
                new Pose(10, 20, 0),
                new Pose(10, 10, -90)
        );

        endTangent = path.evaluateTangent(1.0);
        angleError = PurePursuitController.getAngleError(endTangent, pose);
        assertThat(angleError, equalTo(-10.0));

    }

    @Test
    public void constantsStayConstantTest()
    {
        Pose before = Pose.LEVEL_1_MIDDLE.copy();

        System.out.println("before: " + before);

        Pose pose = new Pose(Vector2.add(Pose.LEVEL_1_MIDDLE.getPosition(), new Vector2(5, 5)), 90);

        Path path = new Path(
                Pose.LEVEL_1_MIDDLE,
                Pose.LEFT_ROCKET_CLOSE
        );

        PurePursuitController.evaluateOutput(pose, path, 1);

        assertThat(Pose.equals(before, Pose.LEVEL_1_MIDDLE), equalTo(true));

        PurePursuitController.evaluateOutput(pose, path, 1);

        assertThat(Pose.equals(before, Pose.LEVEL_1_MIDDLE), equalTo(true));

        System.out.println("after: " + Pose.LEVEL_1_MIDDLE);



        before = Pose.DEFAULT.copy();

        System.out.println("before: " + before);

        pose = new Pose(Vector2.add(Pose.DEFAULT.getPosition(), new Vector2(5, 5)), 135);

        path = new Path(
                Pose.DEFAULT,
                Pose.CARGO_SHIP_MIDDLE_LEFT,
                Pose.LEVEL_1_MIDDLE
        );

        PurePursuitController.evaluateOutput(pose, path, 1);

        assertThat(Pose.equals(before, Pose.DEFAULT), equalTo(true));

        PurePursuitController.evaluateOutput(pose, path, 1);

        assertThat(Pose.equals(before, Pose.DEFAULT), equalTo(true));

        System.out.println("after: " + Pose.DEFAULT);



        before = Pose.ORIGIN.copy();

        System.out.println("before: " + before);

        pose = new Pose(-1, -1, 135);

        path = new Path(
                Pose.ORIGIN,
                Pose.CARGO_SHIP_MIDDLE_LEFT,
                Pose.LEVEL_1_MIDDLE
        );

        PurePursuitController.evaluateOutput(pose, path, 1);

        assertThat(Pose.equals(before, Pose.ORIGIN), equalTo(true));

        PurePursuitController.evaluateOutput(pose, path, 1);

        assertThat(Pose.equals(before, Pose.ORIGIN), equalTo(true));

        System.out.println("after: " + Pose.ORIGIN);
    }
}
