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
    public void constructPathFromStringCorrectlyDeserializesPathString()
    {
        //Two points
        assertEquals(SmellyDashListener.constructPathFromString("0,0,0:10,10,10"),
                new Path(new Pose(0.0, 0.0, 0.0), new Pose(10.0, 10.0, 10.0)));
        //More than two points
        assertEquals(SmellyDashListener.constructPathFromString("0,0,0:10,10,10:4,4,4"),
                new Path(new Pose(0.0, 0.0, 0.0), new Pose(10.0, 10.0, 10.0), new Pose(4,4,4)));
    }
}
