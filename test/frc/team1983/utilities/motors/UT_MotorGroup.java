package frc.team1983.utilities.motors;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.control.MotorGroupController;
import frc.team1983.utilities.sensors.DigitalInputEncoder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_MotorGroup
{
    private MotorGroup motorGroup;

    @Mock
    public Logger logger;

    @Mock
    private MotorGroupController controller;

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

        motorGroup = new MotorGroup("Test Drivebase", FeedbackType.VELOCITY, (Motor) motor1, motor2);
    }

    @Test
    public void disablesControllerOnDisable()
    {
        motorGroup.controller = controller;
        motorGroup.disableController();
        verify(motorGroup.controller, atLeastOnce()).disable();
    }

    @Test
    public void disableControllerDoesNotDisableNullController()
    {
        motorGroup.disableController();
    }

    @Test
    public void disablesControllerOnSetThrottle()
    {
        /*
        motorGroup.controller = controller;

        motorGroup.set(ControlMode.Throttle, 0);
        verify(controller, times(1)).disable();
        verify(motor1, times(1)).set(ControlMode.Throttle, 0);
        verify(motor2, times(1)).set(ControlMode.Throttle, 0);
        */
    }

    @Test
    public void setsMotorList()
    {
        ArrayList<Motor> expected = new ArrayList<>();
        expected.add(motor1);
        expected.add(motor2);

        assertThat(motorGroup.motors, is(expected));
    }

    @Test
    public void correctlyZerosmotorGroup()
    {
        when(motor1.getPosition()).thenReturn(100.0);
        assertThat(motorGroup.getPositionTicks(), is(100.0));
        motorGroup.zero();
        assertThat(motorGroup.getPositionTicks(), is(0.0));
    }

    @Test
    public void usesDigitalInputEncoder()
    {
        //MotorGroup motorGroup = new MotorGroup("Test MotorGroup", FeedbackType.VELOCITY, 1, motor1);
        //assertThat(motorGroup.encoder instanceof DigitalInputEncoder, is(true));
    }

    @Test
    public void castsTalonToEncoder()
    {
        assertThat(motorGroup.encoder, equalTo(motor1));
        verify(motorGroup.encoder, atLeastOnce()).configure();
    }

    @Test
    public void createsControllerOnSetPosition()
    {
        /*
        assertNull(motorGroup.controller);
        motorGroup.set(ControlMode.Position, 0);
        assertNotNull(motorGroup.controller);
        */
    }

    @Test
    public void setsThrottleWhenSetRawThrottle()
    {
        motorGroup.setRawThrottle(0);
        verify(motor1, times(1)).set(0);
        verify(motor2, times(1)).set(0);
    }

    @Test
    public void setsBrakeModeForAllMotors()
    {
        motorGroup.setBrake(false);
        verify(motor1, times(1)).setBrake(false);
        verify(motor2, times(1)).setBrake(false);
    }

    @Test
    public void updatesPGain()
    {
        motorGroup.setKP(1);
        assertThat(motorGroup.getP(), is(1.0));
    }

    @Test
    public void pidGetRespectsFeedbackType()
    {
        motorGroup.pidGet();
        verify(motor1).getVelocity();
    }
}