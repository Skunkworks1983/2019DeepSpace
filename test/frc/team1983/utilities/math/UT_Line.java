package frc.team1983.utilities.math;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_Line
{
    @Test
    public void castTest()
    {
        Line line0 = new Line(0, 0, 0, 1);
        Line line1 = new Line(-10, 10, 1, 0);

        assertThat(Vector2.equals(Line.cast(line0, line1), new Vector2(0, 10)), equalTo(true));

        line0 = new Line(0, 0, 1, 0);
        line1 = new Line(10, 10, 0, 1);

        assertThat(Vector2.equals(Line.cast(line0, line1), new Vector2(10, 0)), equalTo(true));

        line0 = new Line(0, 0, 1, 1);
        line1 = new Line(10, 0, 1, 1);

        assertThat(Line.cast(line0, line1), equalTo(null));
    }

    @Test
    public void closestTest()
    {
        Line line = new Line(0, 0, 1, 1);

        assertThat(Vector2.equals(line.closest(new Vector2(0, 0)), new Vector2(0, 0)), equalTo(true));
        assertThat(Vector2.equals(line.closest(new Vector2(10, 0)), new Vector2(5, 5)), equalTo(true));
        assertThat(Vector2.equals(line.closest(new Vector2(0, 10)), new Vector2(5, 5)), equalTo(true));
        assertThat(Vector2.equals(line.closest(new Vector2(10, 10)), new Vector2(10, 10)), equalTo(true));

        line = new Line(0, 5, 1, 0);

        assertThat(Vector2.equals(line.closest(new Vector2(0, 0)), new Vector2(0, 5)), equalTo(true));
        assertThat(Vector2.equals(line.closest(new Vector2(0, 10)), new Vector2(0, 5)), equalTo(true));
        assertThat(Vector2.equals(line.closest(new Vector2(10, 0)), new Vector2(10, 5)), equalTo(true));
        assertThat(Vector2.equals(line.closest(new Vector2(10, 10)), new Vector2(10, 5)), equalTo(true));
    }
}
