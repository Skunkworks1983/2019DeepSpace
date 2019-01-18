package frc.team1983.utilities.control;

import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_PurePursuitController
{
    @Test
    public void lookAheadTest()
    {
        Pose pose = new Pose(0, 5, 90);
        Path path = new Path(
            new Pose(0, 0, 90),
            new Pose(0, 10, 90)
        );

        assertThat(Vector2.getDistance(new Vector2(0, 8.85), PurePursuitController.evaluateLookAheadPoint(pose, path)) <= 0.01, equalTo(true));
    }

    @Test
    public void outOfBoundsTangentTest()
    {

    }

    @Test
    public void outputTest()
    {

    }

    @Test
    public void radiusTest()
    {
        Vector2 icc = new Vector2(0, 5);

        Pose pose = new Pose(5, 0, 90);
        assertThat(PurePursuitController.evaluateRadiusOfCurvature(pose, icc) < 0, equalTo(true));

        pose = new Pose(-5, 0, 90);
        assertThat(PurePursuitController.evaluateRadiusOfCurvature(pose, icc) > 0, equalTo(true));    }
}
