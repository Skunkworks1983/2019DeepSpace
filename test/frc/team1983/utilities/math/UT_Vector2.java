package frc.team1983.utilities.math;

import frc.team1983.utilities.math.Vector2;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_Vector2
{
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
    }

    @Test
    public void centeringTest()
    {
        Vector2 center1 = Vector2.findCenter(new Vector2(0, 0), new Vector2(1, 0), new Vector2(2, 0));
        System.out.println(center1.toString());

    }
}