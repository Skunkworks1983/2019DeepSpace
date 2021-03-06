package frc.team1983.utilities.math;

import frc.team1983.utilities.pathing.Pose;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UT_Bezier
{
    @Test
    public void evaluateTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.evaluate(0), new Vector2(0, 0)), equalTo(true));
        assertThat(Vector2.equals(b.evaluate(1), new Vector2(1, 1)), equalTo(true));

        b = new Bezier(new Vector2(-10, -4), new Vector2(3, 7), new Vector2(3, 5));

        assertThat(Vector2.equals(b.evaluate(0), new Vector2(-10, -4)), equalTo(true));
        assertThat(Vector2.equals(b.evaluate(1), new Vector2(3, 5)), equalTo(true));
    }

    @Test
    public void evaluateLengthTest()
    {
        Bezier b = new Bezier(new Vector2(0, 10), new Vector2(10, 10));

        assertThat(b.getLength(), equalTo(10.0));
    }

    @Test
    public void evaluateTangentTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.evaluateTangent(0), new Vector2(1, 0)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateTangent(1), new Vector2(0, 1)), equalTo(true));
    }

    @Test
    public void evaluateNormalTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.evaluateNormal(0), new Vector2(0, 1)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateNormal(1), new Vector2(-1, 0)), equalTo(true));
    }

    @Test
    public void evaluatePoseTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Pose.equals(b.evaluatePose(0), new Pose(0, 0, 0)), equalTo(true));
        assertThat(Pose.equals(b.evaluatePose(1.0), new Pose(1, 1, 90)), equalTo(true));
    }

    @Test
    public void offsetTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.offset(0, 1), new Vector2(0, 1)), equalTo(true));
        assertThat(Vector2.equals(b.offset(1, 1), new Vector2(0, 1)), equalTo(true));
    }

    @Test
    public void evaluateCenterOfCurvatureTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1));

        Vector2 point = b.evaluate(0.5);
        Vector2 icc = b.evaluateCenterOfCurvature(0.5);
        assertThat(icc.getX() > point.getX(), equalTo(true));
        assertThat(icc.getY() < point.getY(), equalTo(true));

        b = new Bezier(new Vector2(0, 0), new Vector2(0, 1), new Vector2(-1, 1));

        point = b.evaluate(0.5);
        icc = b.evaluateCenterOfCurvature(0.5);
        assertThat(icc.getX() < point.getX(), equalTo(true));
        assertThat(icc.getY() < point.getY(), equalTo(true));

        b = new Bezier(new Vector2(0, 0), new Vector2(0, 1));

        icc = b.evaluateCenterOfCurvature(0.5);
        assertThat(icc, equalTo(null));
    }

    @Test
    public void evaluateRadiusOfCurvatureTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(0, 1), new Vector2(1, 1));

        Vector2 point = b.evaluate(0.5);
        Vector2 icc = b.evaluateCenterOfCurvature(0.5);
        assertThat(b.evaluateRadiusOfCurvatuve(0.5), equalTo(point.getDistanceTo(icc)));

        b = new Bezier(new Vector2(0, 0), new Vector2(0, 1), new Vector2(-1, 1));

        point = b.evaluate(0.5);
        icc = b.evaluateCenterOfCurvature(0.5);
        assertThat(b.evaluateRadiusOfCurvatuve(0.5), equalTo(point.getDistanceTo(icc)));
    }

    @Test
    public void evaluateClosestPointTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(-1, 0)), new Vector2(0, 0)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(1, 2)), new Vector2(1, 1)), equalTo(true));

        b = new Bezier(new Vector2(0, 0), new Vector2(1, 0));
        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(0.5, -2)), new Vector2(0.5, 0)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(0.5, 2)), new Vector2(0.5, 0)), equalTo(true));
    }

    @Test
    public void equalBeziersAreEqual()
    {
        assertEquals(new Bezier(new Vector2(0, 0), new Vector2(10, 10)),
                new Bezier(new Vector2(0.0, 0.0), new Vector2(10.0, 10.0)));
    }

    @Test
    public void notEqualBeziersAreNotEqual()
    {
        assertNotEquals(new Bezier(new Vector2(0, 0), new Vector2(10, 10)),
                new Bezier(new Vector2(10.0, 10.0), new Vector2(0.0, 0.0)));
        assertNotEquals(new Bezier(new Vector2(0, 0), new Vector2(10, 10), new Vector2(10, 10)),
                new Bezier(new Vector2(0, 0), new Vector2(10, 10)));
    }

    @Test
    public void notBezierIsNotEqualToBeizer()
    {
        assertNotEquals(new Vector2(0, 0), new Bezier(new Vector2(0, 0), new Vector2(10, 10)));
        assertNotEquals(new String(), new Bezier(new Vector2(3, 3), new Vector2(1, 3)));
    }
}