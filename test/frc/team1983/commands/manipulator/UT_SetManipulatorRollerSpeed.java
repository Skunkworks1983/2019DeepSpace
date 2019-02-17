package frc.team1983.commands.manipulator;

import frc.team1983.subsystems.Manipulator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_SetManipulatorRollerSpeed
{
    @Mock
    Manipulator manipulator;

    @Before
    public void setup()
    {
        initMocks(this);
    }

    @Test
    public void initializeSetsMotorSpeeds()
    {
        SetManipulatorRollerSpeed command = new SetManipulatorRollerSpeed(manipulator, .5, .3, false);
        command.initialize();
        verify(manipulator).setLeftRoller(.5);
        verify(manipulator).setRightRoller(.3);

        command = new SetManipulatorRollerSpeed(manipulator, -.5, -.3, false);
        command.initialize();
        verify(manipulator).setLeftRoller(-.5);
        verify(manipulator).setRightRoller(-.3);

        command = new SetManipulatorRollerSpeed(manipulator, .8, false);
        command.initialize();
        verify(manipulator).setLeftRoller(.8);
        verify(manipulator).setRightRoller(.8);
    }

    @Test
    public void isFinishedRespectsZerosOnInterrupt()
    {
        SetManipulatorRollerSpeed command = new SetManipulatorRollerSpeed(manipulator, 0, false);
        assertTrue(command.isFinished());

        command = new SetManipulatorRollerSpeed(manipulator, 1, true);
        assertFalse(command.isFinished());
    }

    @Test
    public void interruptedSetsMotorOutputsToZero()
    {
        SetManipulatorRollerSpeed command = new SetManipulatorRollerSpeed(manipulator, 1, false);
        command.interrupted();
        verify(manipulator).setRollers(0);
    }
}