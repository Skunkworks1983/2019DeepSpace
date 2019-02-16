package frc.team1983.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.team1983.services.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_Manipulator
{
    private Manipulator manipulator;

    @Mock
    DoubleSolenoid hooks;
    @Mock
    DoubleSolenoid extender;
    @Mock
    TalonSRX leftRoller;
    @Mock
    TalonSRX rightRoller;

    @Before
    public void setup()
    {
        initMocks(this);

        manipulator = new Manipulator(extender, hooks, leftRoller, rightRoller, Logger.getInstance());
    }

    @Test
    public void setExtenderSetsTheExtender()
    {
        manipulator.setExtender(true);
        verify(extender).set(DoubleSolenoid.Value.kForward);
        assertTrue(manipulator.isExtended());

        manipulator.setExtender(false);
        verify(extender).set(DoubleSolenoid.Value.kReverse);
        assertFalse(manipulator.isExtended());
    }

    @Test
    public void setHooksSetsTheHooks()
    {
        manipulator.setHooks(true);
        verify(hooks).set(DoubleSolenoid.Value.kForward);
        assertTrue(manipulator.isOpen());

        manipulator.setHooks(false);
        verify(hooks).set(DoubleSolenoid.Value.kReverse);
        assertFalse(manipulator.isOpen());
    }

    @Test
    public void setLeftRollerSetsTheLeftMotor()
    {
        manipulator.setLeftRoller(.5);
        verify(leftRoller).set(ControlMode.PercentOutput, .5);
        manipulator.setLeftRoller(-.5);
        verify(leftRoller).set(ControlMode.PercentOutput, -.5);
        manipulator.setLeftRoller(0);
        verify(leftRoller).set(ControlMode.PercentOutput, 0);
    }

    @Test
    public void setRightRollerSetsTheRightMotor()
    {
        manipulator.setRightRoller(.5);
        verify(rightRoller).set(ControlMode.PercentOutput, .5);
        manipulator.setRightRoller(-.5);
        verify(rightRoller).set(ControlMode.PercentOutput, -.5);
        manipulator.setRightRoller(0);
        verify(rightRoller).set(ControlMode.PercentOutput, 0);
    }

    @Test
    public void setRollersSetsBothMotors()
    {
        manipulator.setRollers(.5);
        verify(rightRoller).set(ControlMode.PercentOutput, .5);
        verify(leftRoller).set(ControlMode.PercentOutput, .5);
        manipulator.setRollers(-.5);
        verify(rightRoller).set(ControlMode.PercentOutput, -.5);
        verify(leftRoller).set(ControlMode.PercentOutput, -.5);
        manipulator.setRollers(0);
        verify(rightRoller).set(ControlMode.PercentOutput, 0);
        verify(leftRoller).set(ControlMode.PercentOutput, 0);
    }
}