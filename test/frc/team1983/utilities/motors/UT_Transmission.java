package frc.team1983.utilities.motors;

import frc.team1983.constants.RobotMap;
import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.control.PIDFController;
import frc.team1983.utilities.sensors.DigitalInputEncoder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_Transmission
{
    private Transmission transmission;

    @Mock
    public Logger logger;

    @Mock
    private PIDFController controller;

    @Mock
    private DigitalInputEncoder dioEncoder;

    @Mock
    private Talon motor1;
    @Mock
    private Talon motor2;

    @Before
    public void setup()
    {
        initMocks(this);

        transmission = new Transmission("Test Drivebase", FeedbackType.VELOCITY, (Motor) motor1, motor2);
        transmission.setTicksPerInch(100);
    }

    @Test
    public void disablesControllerOnDisable()
    {
        transmission.controller = controller;
        transmission.disableController();
        verify(transmission.controller, atLeastOnce()).disable();
    }

    @Test
    public void disablesControllerOnSetThrottle()
    {
        transmission.controller = controller;

        transmission.set(ControlMode.Throttle, 0);
        verify(controller, times(1)).disable();
        verify(motor1, times(1)).set(ControlMode.Throttle, 0);
        verify(motor2, times(1)).set(ControlMode.Throttle, 0);
    }

    @Test
    public void setsMotorList()
    {
        ArrayList<Motor> expected = new ArrayList<>();
        expected.add(motor1);
        expected.add(motor2);

        assertThat(transmission.motors, is(expected));
    }

    @Test
    public void correctlyZerosTransmission()
    {
        when(motor1.getPosition()).thenReturn(100.0);
        assertThat(transmission.getPositionInches(), is(1.0));
        transmission.zero();
        assertThat(transmission.getPositionInches(), is(0.0));
    }

    @Test
    public void usesDigitalInputEncoder()
    {
        Transmission transmission = new Transmission("Test Transmission", FeedbackType.VELOCITY, 1, motor1);
        assertThat(transmission.encoder instanceof DigitalInputEncoder, is(true));
    }

    @Test
    public void castsTalonToEncoder()
    {
        assertThat(transmission.encoder, equalTo(motor1));
        verify(transmission.encoder, atLeastOnce()).configure();
    }

    @Test
    public void createsControllerOnSetPosition()
    {
        assertNull(transmission.controller);
        transmission.set(ControlMode.Position, 0);
        assertNotNull(transmission.controller);
    }

    @Test
    public void setsThrottleWhenSetRawThrottle()
    {
        transmission.setRawThrottle(0);
        verify(motor1, times(1)).set(ControlMode.Throttle, 0);
        verify(motor2, times(1)).set(ControlMode.Throttle, 0);
    }

    @Test
    public void setsBrakeModeForAllMotors()
    {
        transmission.setBrake(false);
        verify(motor1, times(1)).setBrake(false);
        verify(motor2, times(1)).setBrake(false);
    }

    @Test
    public void updatesPIDGains()
    {
        transmission.setPID(1, 2, 3);
        assertThat(transmission.getP(), is(1.0));
        assertThat(transmission.getI(), is(2.0));
        assertThat(transmission.getD(), is(3.0));
    }

    @Test
    public void convertsTicksAndInches()
    {
        assertThat(transmission.toTicks(1.0), equalTo(100.0));
        assertThat(transmission.toTicks(1.5), equalTo(150.0));
        assertThat(transmission.toTicks(2.0), equalTo(200.0));

        assertThat(transmission.toInches(200.0), equalTo(2.0));
        assertThat(transmission.toInches(300.0), equalTo(3.0));
        assertThat(transmission.toInches(400.0), equalTo(4.0));
    }
}