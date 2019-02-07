package frc.team1983.utilities.motion;

import org.junit.Test;

public class UT_MotionProfile
{
    @Test
    public void displacementTest()
    {
        MotionProfile motionProfile = MotionProfile.generateMotionProfile(0, 1000, 500, 100);

        System.out.println(motionProfile.getLength());

        System.out.println(motionProfile.calculatePosition(0));
    }
}
