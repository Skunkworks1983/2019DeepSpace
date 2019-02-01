package frc.team1983.utilities.pathing;

import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Vector2;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UT_Path
{
    @Test
    public void getLengthTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 5, 90),
                new Pose(0, 10, 90)
        );

        assertThat(path.getLength(), equalTo(10.0));
    }

    @Test
    public void evaluateTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 5, 90),
                new Pose(0, 10, 90)
        );

        assertThat(Vector2.equals(path.evaluate(0.0), new Vector2(0, 0)), equalTo(true));
        assertThat(Vector2.equals(path.evaluate(0.5), new Vector2(0, 5)), equalTo(true));
        assertThat(Vector2.equals(path.evaluate(1.0), new Vector2(0, 10)), equalTo(true));
    }

    @Test
    public void evaluateTangentTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 5, 90),
                new Pose(10, 10, 0)
        );

        assertThat(Vector2.equals(path.evaluateTangent(0.0), new Vector2(0.0, 1.0)), equalTo(true));
        assertThat(Vector2.equals(path.evaluateTangent(1.0), new Vector2(1.0, 0.0)), equalTo(true));
    }

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

    @Test
    public void evaluateClosestPointTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 2, 90),
                new Pose(0, 4, 90)
        );

        Vector2 point = new Vector2(0, -10);

        double closestT = path.evaluateClosestT(point);
        Vector2 closestPoint = path.evaluateClosestPoint(point);

        assertThat(Vector2.equals(closestPoint, new Vector2(0, 0.0)), equalTo(true));
        assertThat(closestT, equalTo(0.0));

        point = new Vector2(0, 2);

        closestT = path.evaluateClosestT(point);
        closestPoint = path.evaluateClosestPoint(point);

        assertThat(Vector2.equals(closestPoint, new Vector2(0, 2.0)), equalTo(true));
        assertThat(closestT, equalTo(0.5));

        point = new Vector2(0, 3);

        closestT = path.evaluateClosestT(point);
        closestPoint = path.evaluateClosestPoint(point);

        assertThat(Vector2.equals(closestPoint, new Vector2(0, 3.0)), equalTo(true));
        assertThat(closestT, equalTo(0.75));
    }

    @Test
    public void equalPathsAreEqual()
    {
        assertEquals(new Path(new Pose(1, 2, 3)),
                new Path(new Pose(1.0,2.0, 3.0)));
        assertEquals(new Path(new Pose(0, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)),
                new Path(new Pose(0, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)));
    }

    @Test
    public void notEqualBeziersAreNotEqual()
    {
        assertNotEquals(new Path(new Pose(10, 20, 30)),
                new Path(new Pose(1.0,2.0, 3.0)));
        assertNotEquals(new Path(new Pose(0.5, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)),
                new Path(new Pose(0, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)));
    }

    @Test
    public void notBezierIsNotEqualToBeizer()
    {
        assertNotEquals(new String(), new Path(new Pose(10, 20, 30)));
    }
}
