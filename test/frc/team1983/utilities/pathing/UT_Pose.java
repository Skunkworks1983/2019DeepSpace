package frc.team1983.utilities.pathing;

import frc.team1983.constants.Constants;
import frc.team1983.utilities.math.Vector2;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_Pose
{
    @Test
    public void headingTest()
    {
        assertThat(new Pose(10, 10, 45).getHeading(), equalTo(45.0));
        assertThat(Vector2.equals(new Pose(new Vector2(0, 0), new Vector2(1, 1)).getDirection(),
                                           new Vector2(1, 1).getNormalized()), equalTo(true));
        assertThat(new Pose(Vector2.ZERO, new Vector2(1, 1)).getHeading(), equalTo(45.0));
        assertThat(Vector2.equals(new Pose(new Vector2(0, 0), 45.0).getDirection(),
                                           new Vector2(1, 1).getNormalized()), equalTo(true));
    }

    @Test
    public void getReversed()
    {
        Pose p = new Pose(10, 10, 90);

        assertThat(Math.abs(p.getReversed().getHeading() + 90) < Constants.EPSILON, equalTo(true));
    }

    @Test
    public void printPoses()
    {
        System.out.println(Pose.LEFT_LOADING_STATION_LINE_UP);
        System.out.println(Pose.LEFT_LOADING_STATION_DRIVER_SWITCH);
        System.out.println("");
        System.out.println(Pose.RIGHT_LOADING_STATION_LINE_UP);
        System.out.println(Pose.RIGHT_LOADING_STATION_DRIVER_SWITCH);
    }

    @Test
    public void translateRelativeTest()
    {
        System.out.println(new Pose(0, 0, 90).translateRelative(0, 1).toString());
        System.out.println(Pose.RIGHT_ROCKET_FAR.translateRelative(-2, 0));
    }
}