package frc.team1983.utilities.pathing;

import frc.team1983.utilities.math.Vector2;
import org.junit.Test;

public class UT_Path
{
    @Test
    public void getPath()
    {
        Pose pose1 = new Pose(0, 0, 90);
        Pose pose2 = new Pose(0, 10, 90);

        Path p = new Path(
                pose1,
                pose2
        );

        System.out.println(pose1.getHeading());

        for(double i = 0; i < Path.RESOLUTION; i++)
            System.out.println(p.evaluate(i / Path.RESOLUTION));
    }
}
