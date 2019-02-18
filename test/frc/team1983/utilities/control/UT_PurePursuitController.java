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
                new Pose(0, 0, 90),
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

        pose = new Pose(0, 10, 0);

        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));
        assertNotEquals(PurePursuitController.evaluateOutput(pose, path, 1).getValue1(), 0.0);
        assertNotEquals(PurePursuitController.evaluateOutput(pose, path, 1).getValue2(), 0.0);
    }

    @Test
    public void reverseTest()
    {
        Pose pose = new Pose(0, 0, 90);

        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        assertThat(PurePursuitController.evaluateOutput(pose, path, -1).getValue1() < 0, equalTo(true));
        assertThat(PurePursuitController.evaluateOutput(pose, path, -1).getValue2() < 0, equalTo(true));
    }

    @Test
    public void pastPathTest()
    {
        Pose pose = new Pose(0, 11, 90);

        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        assertThat(PurePursuitController.evaluateOutput(pose, path, 1).getValue1() < 0, equalTo(true));
        assertThat(PurePursuitController.evaluateOutput(pose, path, 1).getValue2() < 0, equalTo(true));
    }
}
