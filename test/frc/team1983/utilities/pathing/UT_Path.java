package frc.team1983.utilities.pathing;

import org.junit.Test;

public class UT_Path
{
    @Test
    public void evaluatePoints()
    {
        Path path = new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90),
                new Pose(-5, 10, -90)
        );

        for (double i = 0; i < Path.RESOLUTION; i++)
            System.out.println(path.evaluate(i / Path.RESOLUTION));

    }
}
