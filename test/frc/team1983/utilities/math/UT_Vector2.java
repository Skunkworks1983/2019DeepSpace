package frc.team1983.utilities.math;

import org.junit.Test;

import static frc.team1983.constants.Constants.EPSILON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class UT_Vector2
{
    @Test
    public void toStringTest()
    {
        Vector2 v = new Vector2(10, 5);

        assertThat(v.toString().equals("<10.0, 5.0>"), equalTo(true));
        assertThat(v.toString(true).equals("10.0, 5.0"), equalTo(true));

        v = new Vector2(-5, -10);

        assertThat(v.toString().equals("<-5.0, -10.0>"), equalTo(true));
        assertThat(v.toString(true).equals("-5.0, -10.0"), equalTo(true));
    }

    @Test
    public void normalizeTest()
    {
        Vector2 v = new Vector2(0, 10);

        assertThat(v.getNormalized().equals(new Vector2(0, 1)), equalTo(true));

        v = new Vector2(-10, 0);
        assertThat(v.getNormalized().equals(new Vector2(-1, 0)), equalTo(true));

        v = new Vector2(10, 10);
        assertThat(v.getNormalized().equals(new Vector2(Math.sqrt(2.0) / 2.0, Math.sqrt(2.0) / 2.0)), equalTo(true));
    }

    @Test
    public void negativeTest()
    {
        Vector2 v = new Vector2(10, 10);

        assertThat(v.getNegative().equals(new Vector2(-10, -10)), equalTo(true));

        v = new Vector2(-10, -10);
        assertThat(v.getNegative().equals(new Vector2(10, 10)), equalTo(true));
    }

    @Test
    public void leftTest()
    {
        Vector2 v = new Vector2(5, 10);

        assertThat(v.getLeft().equals(new Vector2(-10, 5)), equalTo(true));

        v = new Vector2(10, 5);
        assertThat(v.getLeft().equals(new Vector2(-5, 10)), equalTo(true));
    }

    @Test
    public void rightTest()
    {
        Vector2 v = new Vector2(5, 10);

        assertThat(v.getRight().equals(new Vector2(10, -5)), equalTo(true));

        v = new Vector2(10, 5);
        assertThat(v.getRight().equals(new Vector2(5, -10)), equalTo(true));
    }

    @Test
    public void equalsTest()
    {
        Vector2 v1 = new Vector2(10.0, 10.0);
        Vector2 v2 = new Vector2(10.0, 10.0);

        assertThat(v1.equals(v2), equalTo(true));
    }

    @Test
    public void additionTest()
    {
        assertThat(Vector2.equals(
                Vector2.add(new Vector2(1, 2), new Vector2(3, 4)),
                new Vector2(4, 6)),
                equalTo(true));

        assertThat(Vector2.equals(
                Vector2.add(new Vector2(-5, 6), new Vector2(3, -4)),
                new Vector2(-2, 2)),
                equalTo(true));

        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(3, 4);
        v1.add(v2);

        assertThat(v1.equals(new Vector2(4, 6)), equalTo(true));
    }

    @Test
    public void subtractionTest()
    {
        assertThat(Vector2.equals(
                Vector2.sub(new Vector2(4, 7), new Vector2(1, 3)),
                new Vector2(3, 4)),
                equalTo(true));

        assertThat(Vector2.equals(
                Vector2.sub(new Vector2(9, -3), new Vector2(-5, 9)),
                new Vector2(14, -12)),
                equalTo(true));

        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(3, 4);
        v1.sub(v2);

        assertThat(v1.equals(new Vector2(-2, -2)), equalTo(true));
    }

    @Test
    public void scalingTest()
    {
        assertThat(Vector2.equals(
                Vector2.scale(new Vector2(4, 6), 1.5),
                new Vector2(6, 9)),
                equalTo(true));

        assertThat(Vector2.equals(
                Vector2.scale(new Vector2(5, 2), -1),
                new Vector2(-5, -2)),
                equalTo(true));
    }

    @Test
    public void magnitudeTest()
    {
        assertThat(new Vector2(0, 10).getMagnitude(), equalTo(10.0));
        assertThat(new Vector2(0, -10).getMagnitude(), equalTo(10.0));
        assertThat(new Vector2(3, 4).getMagnitude(), equalTo(5.0));
    }

    @Test
    public void normalizingTest()
    {
        assertThat(Vector2.equals(new Vector2(10, 10).getNormalized(), new Vector2(1, 1).getNormalized()), equalTo(true));
        assertThat(Vector2.equals(new Vector2(-10, -10).getNormalized(), new Vector2(-1, -1).getNormalized()), equalTo(true));
    }

    @Test
    public void distanceTest()
    {
        assertThat(Vector2.getDistance(new Vector2(0, 0), new Vector2(10, 0)), equalTo(10.0));
        assertThat(Vector2.getDistance(new Vector2(0, 0), new Vector2(1, 0)), equalTo(1.0));
        assertThat(Vector2.getDistance(new Vector2(0, 0), new Vector2(1, 1)), equalTo(Math.sqrt(2)));
        assertThat(Vector2.getDistance(new Vector2(1, 1), new Vector2(2, 2)), equalTo(Math.sqrt(2)));
        assertThat(Vector2.getDistance(new Vector2(-1, 0), new Vector2(-10, 0)), equalTo(9.0));
    }

    @Test
    public void twistingTest()
    {
        assertThat(Vector2.equals(Vector2.twist(new Vector2(1, 0), new Vector2(0, 0), 90), new Vector2(0, 1)), equalTo(true));
        assertThat(Vector2.equals(Vector2.twist(new Vector2(0, 1), new Vector2(0, 0), 90), new Vector2(-1, 0)), equalTo(true));
        assertThat(Vector2.equals(Vector2.twist(new Vector2(-1, 0), new Vector2(0, 0), 90), new Vector2(0, -1)), equalTo(true));
        assertThat(Vector2.equals(Vector2.twist(new Vector2(0, 0), new Vector2(1, 0), -90), new Vector2(1, 1)), equalTo(true));

        Vector2 v1 = new Vector2(1, 0);
        Vector2 v2 = new Vector2(0, 0);
        v1.twist(v2, 90);

        assertThat(v1.equals(new Vector2(0, 1)), equalTo(true));
    }

    @Test
    public void centeringTest()
    {
        Vector2[] vectors = new Vector2[] {new Vector2(0, 0), new Vector2(1, 0), new Vector2(2, 0)};
        Vector2 c = Vector2.findCenter(vectors);

        Vector2 center1 = Vector2.findCenter(new Vector2(0, 0), new Vector2(1, 0), new Vector2(2, 0));
        assertThat(Vector2.equals(center1, new Vector2(1, 0)), equalTo(true));

        Vector2 center2 = Vector2.findCenter(new Vector2(-1, 0), new Vector2(0, 0), new Vector2(1, 0));
        assertThat(Vector2.equals(center2, Vector2.ZERO), equalTo(true));
    }

    @Test
    public void equalVectorsAreEqual()
    {
        assertEquals(new Vector2(10, 10), new Vector2(10, 10));
        assertEquals(new Vector2(10 + EPSILON, 10), new Vector2(10, 10));
        assertEquals(new Vector2(10, 10 - EPSILON), new Vector2(10 + EPSILON, 10));
    }
}