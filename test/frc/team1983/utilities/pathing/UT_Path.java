package frc.team1983.utilities.pathing;

import frc.team1983.utilities.math.Vector2;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UT_Path
{
    @Test
    public void reverseTest()
    {
        Path path = new Path(
                true,
                new Pose(0, 0, 90),
                new Pose(10, 10, 0)
        );

        assertThat(path.evaluateTangent(0).equals(new Vector2(0, -1)), equalTo(true));
        assertThat(path.evaluateTangent(1).equals(new Vector2(-1, 0)), equalTo(true));
    }

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

        path = new Path(
                Pose.ORIGIN,
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

        path = new Path(
                Pose.ORIGIN,
                new Pose(0, 5, 90),
                new Pose(10, 10, 0)
        );

        assertThat(Vector2.equals(path.evaluateTangent(0.0), new Vector2(0.0, 1.0)), equalTo(true));
        assertThat(Vector2.equals(path.evaluateTangent(1.0), new Vector2(1.0, 0.0)), equalTo(true));
    }

    @Test
    public void evaluatePoseTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 5, 90),
                new Pose(10, 10, 0)
        );

        assertThat(Pose.equals(path.evaluatePose(0), new Pose(0, 0, 90)), equalTo(true));
        assertThat(Pose.equals(path.evaluatePose(1.0), new Pose(10, 10, 0)), equalTo(true));
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
    public void evaluateNormalTest()
    {
        Path path = new Path(
                new Pose(0, 0, 0),
                new Pose(10, 10, 90)
        );

        assertThat(Vector2.equals(path.evaluateNormal(0), new Vector2(0, 1)), equalTo(true));
        assertThat(Vector2.equals(path.evaluateNormal(1), new Vector2(-1, 0)), equalTo(true));
    }

    @Test
    public void centerOfCurvatureTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(10, 10, 0)
        );

        Vector2 point = path.evaluate(0.5);
        Vector2 icc = path.evaluateCenterOfCurvature(0.5);
        assertThat(icc.getX() > point.getX(), equalTo(true));
        assertThat(icc.getY() < point.getY(), equalTo(true));

        path = new Path(
                new Pose(0, 0, 90),
                new Pose(-10, 10, 180)
        );

        point = path.evaluate(0.5);
        icc = path.evaluateCenterOfCurvature(0.5);
        assertThat(icc.getX() < point.getX(), equalTo(true));
        assertThat(icc.getY() < point.getY(), equalTo(true));

        path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        icc = path.evaluateCenterOfCurvature(0.5);
        assertThat(icc, equalTo(null));
    }

    @Test
    public void radiusOfCurvatureTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(10, 10, 0)
        );

        Vector2 point = path.evaluate(0.5);
        Vector2 icc = path.evaluateCenterOfCurvature(0.5);
        assertThat(path.evaluateRadiusOfCurvatuve(0.5), equalTo(point.getDistanceTo(icc)));

        path = new Path(
                new Pose(0, 0, 90),
                new Pose(-10, 10, 180)
        );

        point = path.evaluate(0.5);
        icc = path.evaluateCenterOfCurvature(0.5);
        assertThat(path.evaluateRadiusOfCurvatuve(0.5), equalTo(point.getDistanceTo(icc)));
    }

    @Test
    public void evaluateClosestPointTest()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(10, 10, 90),
                new Pose(0, 20, 90),
                new Pose(10, 30, 90)
        );

        Vector2 point = new Vector2(5, 15);

        double closestT = path.evaluateClosestT(point);
        Vector2 closestPoint = path.evaluateClosestPoint(point);

        assertThat(closestPoint.getY() > 10.0, equalTo(true));
        assertThat(closestPoint.getY() < 20.0, equalTo(true));
        assertThat(closestT > 0.0, equalTo(true));
        assertThat(closestT > 0.33, equalTo(true));

        point = new Vector2(0, 25);

        closestT = path.evaluateClosestT(point);
        closestPoint = path.evaluateClosestPoint(point);

        assertThat(closestPoint.getY() > 20.0, equalTo(true));
        assertThat(closestPoint.getY() < 30.0, equalTo(true));
        assertThat(closestT > 0.33, equalTo(true));
        assertThat(closestT > 0.66, equalTo(true));

        path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        );

        point = new Vector2(0, 5);

        closestT = path.evaluateClosestT(point);
    }

    @Test
    public void equalPathsAreEqual()
    {
        assertEquals(new Path(new Pose(1, 2, 3), new Pose(5, 6, 7)),
                new Path(new Pose(1.0,2.0, 3.0), new Pose(5, 6, 7)));
        Pose[] poses = {new Pose(6, 6, 6), new Pose(1, 3, 5)};
        assertEquals(new Path(new Pose(1, 2, 3), new Pose(5, 6, 7), poses),
                new Path(new Pose(1.0,2.0, 3.0), new Pose(5, 6, 7), poses));
        assertEquals(new Path(new Pose(0, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)),
                new Path(new Pose(0, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)));
    }

    @Test
    public void notEqualPathsAreNotEqual()
    {
        assertNotEquals(new Path(new Pose(10, 20, 30), new Pose(0,0,0), new Pose(12, 3, 1)),
                new Path(new Pose(1.0,2.0, 3.0), new Pose(0.0, 0.0, 0.0)));
        assertNotEquals(new Path(new Pose(10, 20, 30), new Pose(0,0,0), new Pose(12, 3, 1)),
                new Path(new Pose(1.0,2.0, 3.0), new Pose(2, 3, 1)));
        assertNotEquals(new Path(new Pose(0.5, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)),
                new Path(new Pose(0, 0, 0), new Pose(10, 10, 10), new Pose(0, 0, 0)));
    }

    @Test
    public void notPathIsNotEqualToPath()
    {
        assertNotEquals(new String(), new Path(new Pose(10, 20, 30), new Pose(1, 1, 1)));
    }

    @Test
    public void constructingWithMoreThanTwoPosesCreatesTheCorrectNumberOfBeziers()
    {
        assertEquals(2, new Path(new Pose(0, 0, 0), new Pose(5, 5, 5), new Pose(10, 10, 10)).curves.length);

        Pose[] poses = {new Pose(3, 3, 3), new Pose(1, 1, 1)};
        assertEquals(3, new Path(new Pose(0, 0, 0), new Pose(2, 2, 2), poses).curves.length);
    }
}
