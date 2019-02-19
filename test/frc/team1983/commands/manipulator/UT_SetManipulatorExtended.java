package frc.team1983.commands.manipulator;

import frc.team1983.subsystems.Manipulator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_SetManipulatorExtended
{
    @Mock
    Manipulator manipulator;

    @Before
    public void setup()
    {
        initMocks(this);
    }

    @Test
    public void initializeSetsTheExtender()
    {
        /*
        SetManipulatorExtended command = new SetManipulatorExtended(manipulator, true);
        command.initialize();
        verify(manipulator).setExtender(true);

        command = new SetManipulatorExtended(manipulator, false);
        command.initialize();
        verify(manipulator).setExtender(false);
        */
    }

    @Test
    public void initializeTogglesTheExtender()
    {
        /*
        when(manipulator.isExtended()).thenReturn(false);
        SetManipulatorExtended command = new SetManipulatorExtended(manipulator);
        command.initialize();
        verify(manipulator).setExtender(true);

        when(manipulator.isExtended()).thenReturn(true);
        command = new SetManipulatorExtended(manipulator);
        command.initialize();
        verify(manipulator).setExtender(false);
        */
    }
}