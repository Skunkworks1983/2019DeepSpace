package frc.team1983.utilities.motion;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UT_MotionProfile
{
    @Test
    public void forwardTest()
    {
        TrapezoidalVelocityProfile profile = new TrapezoidalVelocityProfile(1000, 100, 100);

        assertThat(profile.evaluateDisplacement(0), equalTo(0.0));
        assertThat(profile.evaluateDisplacement(profile.getDuration() / 2), equalTo(500.0));
        assertThat(profile.evaluateDisplacement(profile.getDuration()), equalTo(1000.0));

        assertThat(profile.evaluateVelocity(0), equalTo(0.0));
        assertThat(profile.evaluateVelocity(profile.getDuration() / 2), equalTo(100.0));
        assertThat(profile.evaluateVelocity(profile.getDuration()), equalTo(0.0));

        assertThat(profile.evaluateAcceleration(0.5), equalTo(100.0));
        assertThat(profile.evaluateAcceleration(5.5), equalTo(0.0));
        assertThat(profile.evaluateAcceleration(10.5), equalTo(-100.0));
    }

    @Test
    public void reverseTest()
    {
        TrapezoidalVelocityProfile profile = new TrapezoidalVelocityProfile(-1000, -100, -100);

        assertThat(profile.evaluateDisplacement(0), equalTo(0.0));
        assertThat(profile.evaluateDisplacement(profile.getDuration() / 2), equalTo(-500.0));
        assertThat(profile.evaluateDisplacement(profile.getDuration()), equalTo(-1000.0));

        assertThat(profile.evaluateVelocity(0), equalTo(0.0));
        assertThat(profile.evaluateVelocity(profile.getDuration() / 2), equalTo(-100.0));
        assertThat(profile.evaluateVelocity(profile.getDuration()), equalTo(0.0));

        assertThat(profile.evaluateAcceleration(0.5), equalTo(-100.0));
        assertThat(profile.evaluateAcceleration(5.5), equalTo(0.0));
        assertThat(profile.evaluateAcceleration(10.5), equalTo(100.0));
    }
}
