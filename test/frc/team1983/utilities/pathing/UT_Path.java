package frc.team1983.utilities.pathing;

import org.junit.Test;

public class UT_Path
{
    @Test
    public void getSegmentTest()
    {
        Path path = new Path(new Pose(0, 0, 0), new Pose(10, 10, 0), new Pose(20, 20, 0));
        System.out.println(path.evaluate(0));
    }
}
