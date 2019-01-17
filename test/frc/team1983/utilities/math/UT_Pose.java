package frc.team1983.utilities.math;

import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Pose;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_Pose
{
    @Test
    public void evaluateHeading()
    {
        Pose p = new Pose(new Vector2(0, 0), new Vector2(0, 1));

        System.out.println(p.getHeading());
    }
}