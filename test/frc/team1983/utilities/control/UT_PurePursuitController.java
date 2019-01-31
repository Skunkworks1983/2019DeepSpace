package frc.team1983.utilities.control;

import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_PurePursuitController
{
    @Test
    public void outputTest()
    {

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
        double DEADZONE = PurePursuitController.VELOCITY_DEADZONE;

        Pose pose = new Pose(0, 10, 90);

        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        pose = new Pose(0, 10 + DEADZONE - 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        pose = new Pose(0, 10 - DEADZONE + 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(true));

        pose = new Pose(0, 10 + DEADZONE + 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 10 - DEADZONE - 1e-3, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 0, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));

        pose = new Pose(0, 20, 90);
        assertThat(PurePursuitController.inDeadzone(pose, path), equalTo(false));
    }
}
