package frc.team1983.utilities.motion;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UT_MotionProfile
{
    @Test
    public void forwardPositionTest()
    {
        MotionProfile profile = MotionProfile.generateProfile(0, 1000, 100, 100);

        assertThat(profile.evaluate(0), equalTo(0.0));
        assertThat(profile.evaluate(profile.getDuration() / 2), equalTo(500.0));
        assertThat(profile.evaluate(profile.getDuration()), equalTo(1000.0));

    }

    @Test
    public void reversePositionTest()
    {
        MotionProfile profile = MotionProfile.generateProfile(0, -1000, 100, 100);

        assertThat(profile.evaluate(0), equalTo(0.0));
        assertThat(profile.evaluate(profile.getDuration() / 2), equalTo(-500.0));
        assertThat(profile.evaluate(profile.getDuration()), equalTo(-1000.0));

//        assertThat(profile.evaluateAcceleration(0.5), equalTo(-100.0));
//        assertThat(profile.evaluateAcceleration(5.5), equalTo(0.0));
//        assertThat(profile.evaluateAcceleration(10.5), equalTo(100.0));
    }
}
