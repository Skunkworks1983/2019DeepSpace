package frc.team1983.commands;

import frc.team1983.commands.drivebase.SmellyDashListener;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;


public class UT_SmellyDashListener
{
    @Before
    public void setup()
    {
        initMocks(this);
    }

    @Test
    public void constructPathFromStringCorrectlyDeserializesPathStringWithOnlyTwoPoses()
    {
        //Two points
        Path path1 = SmellyDashListener.constructPathFromString("0,2,4:10,10,10");
        Path path2 = new Path(new Pose(0.0, 2.0, 4.0), new Pose(10.0, 10.0, 10.0));
        System.out.println(path1.getLength());
        System.out.println(path2.getLength());
        assertEquals(path1, path2);
    }

    @Test
    public void constructPathFromStringCorrectlyDeserializesPathStringsWithOnlyThreePoses()
    {
        //More than two points
        Path path1 = SmellyDashListener.constructPathFromString("3,8,180:10,10,10:4,5,4");
        Path path2 = new Path(new Pose(3.0, 8.0, 180), new Pose(10.0, 10.0, 10.0), new Pose(4.0,5.0,4.0));
        System.out.println(path1.getLength());
        System.out.println(path2.getLength());
        assertEquals(path1, path2);
    }

    @Test
    public void constructPathFromStringCorrectlyDeserializesPathStringsWithMoreThanThreePoses()
    {
        Path path1 = new Path(new Pose(0.0, 0.0, 0.0), new Pose(10.0, 10.0, 10.0),
                new Pose(4.0,4.0,4.0), new Pose(5.0, 5.0, 5.0));
        Path path2 = SmellyDashListener.constructPathFromString("0,0,0:10,10,10:4,4,4:5,5,5");
        System.out.println(path1.getLength());
        System.out.println(path2.getLength());
        assertEquals(path1, path2);
    }
}
