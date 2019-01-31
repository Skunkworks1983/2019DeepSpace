package frc.team1983.utilities.math;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
    public void lengthTest()
    {
        Bezier b = new Bezier(new Vector2(0, 10), new Vector2(10, 10));

        assertThat(b.getLength(), equalTo(10.0));
    }

    @Test
    public void tangentTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.evaluateTangent(0), new Vector2(1, 0)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateTangent(1), new Vector2(0, 1)), equalTo(true));
    }

    @Test
    public void normalTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.evaluateNormal(0), new Vector2(0, 1)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateNormal(1), new Vector2(-1, 0)), equalTo(true));
    }

    @Test
    public void offsetTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.offset(0, 1), new Vector2(0, 1)), equalTo(true));
        assertThat(Vector2.equals(b.offset(1, 1), new Vector2(0, 1)), equalTo(true));
    }

    @Test
    public void centerOfCurvatureTest()
    {

    }

    @Test
    public void radiusOfCurvatureTest()
    {

    }

    @Test
    public void closestPointTest()
    {
        Bezier b = new Bezier(new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1));

        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(-1, 0)), new Vector2(0, 0)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(1, 2)), new Vector2(1, 1)), equalTo(true));

        b = new Bezier(new Vector2(0, 0), new Vector2(1, 0));
        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(0.5, -2)), new Vector2(0.5, 0)), equalTo(true));
        assertThat(Vector2.equals(b.evaluateClosestPoint(new Vector2(0.5, 2)), new Vector2(0.5, 0)), equalTo(true));
    }
}