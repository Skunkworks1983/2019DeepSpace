package frc.team1983.utilities.pathing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_Path
{
    @Test
    public void getCurveTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90),
                new Pose(0, 20, 90)
        );

        assertThat(path.getCurve(0).equals(path.curves[0]), equalTo(true));
        assertThat(path.getCurve(0.5).equals(path.curves[0]), equalTo(true));
        assertThat(path.getCurve(1).equals(path.curves[1]), equalTo(true));
    }

    @Test
    public void evaluateLengthToCurveTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90),
                new Pose(0, 20, 90)
        );

        assertThat(path.evaluateLengthToCurve(path.curves[0]), equalTo(0.0));
        assertThat(path.evaluateLengthToCurve(path.curves[1]), equalTo(10.0));
    }

    @Test
    public void evaluateLengthToTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90),
                new Pose(0, 20, 90)
        );

        assertThat(path.evaluateLengthTo(0.0), equalTo(0.0));
        assertThat(path.evaluateLengthTo(0.25), equalTo(5.0));
        assertThat(path.evaluateLengthTo(0.5), equalTo(10.0));
        assertThat(path.evaluateLengthTo(0.75), equalTo(15.0));
        assertThat(path.evaluateLengthTo(1.0), equalTo(20.0));
    }
}
