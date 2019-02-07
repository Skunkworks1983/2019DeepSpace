package frc.team1983.utilities.motion;

import org.junit.Test;

import static frc.team1983.constants.Constants.EPSILON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

public class UT_IMotionProfile
{
    @Test
    public void trapezoidalDisplacement()
    {
        IMotionProfile motionProfile = IMotionProfile.generateMotionProfile(0, 1000, 200, 100);
        assertThat(motionProfile.calcPos(0), equalTo(0.0));
        assertThat(motionProfile.calcPos(motionProfile.getLength() / 2), equalTo(500.0));
        assertThat(motionProfile.calcPos(motionProfile.getLength()), equalTo(1000.0));
    }

    @Test
    public void triangularDisplacement()
    {
        IMotionProfile motionProfile = IMotionProfile.generateMotionProfile(0, 1000, 1000, 500);
        assertThat(motionProfile.calcPos(0), equalTo(0.0));
        assertEquals(motionProfile.calcPos(motionProfile.getLength() * .5), 500.0, EPSILON);
        assertThat(motionProfile.calcPos(motionProfile.getLength()), equalTo(1000.0));
    }
}
