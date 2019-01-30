package frc.team1983.services;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UT_OI
{
    @Test
    public void scaleTest()
    {
        double DEADZONE = OI.JOYSTICK_DEADZONE;
        double EXPONENT = OI.JOYSTICK_EXPONENT;

        assertThat(OI.scale(0.0), equalTo(0.0));
        assertThat(OI.scale(DEADZONE), equalTo(0.0));
        assertThat(OI.scale(DEADZONE), equalTo(0.0));

        assertThat(OI.scale(0.25), equalTo(Math.abs(Math.pow(0.25, EXPONENT))));
        assertThat(OI.scale(-0.25), equalTo(-Math.abs(Math.pow(0.25, EXPONENT))));

        assertThat(OI.scale(0.5), equalTo(Math.abs(Math.pow(0.5, EXPONENT))));
        assertThat(OI.scale(-0.5), equalTo(-Math.abs(Math.pow(0.5, EXPONENT))));

        assertThat(OI.scale(1.0), equalTo(Math.abs(Math.pow(1.0, EXPONENT))));
        assertThat(OI.scale(-1.0), equalTo(-Math.abs(Math.pow(1.0, EXPONENT))));
    }
}
