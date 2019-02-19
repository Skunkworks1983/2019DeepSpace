package frc.team1983.commands.manipulator;

import frc.team1983.subsystems.Manipulator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_OpenHooks
{
    @Mock
    Manipulator manipulator;

    @Before
    public void setup()
    {
        initMocks(this);
    }

    @Test
    public void initializeSetsTheHooks()
    {
        OpenHooks command = new OpenHooks(manipulator, true);
        command.initialize();
        verify(manipulator).setHooks(true);

        command = new OpenHooks(manipulator, false);
        command.initialize();
        verify(manipulator).setHooks(false);
    }

    @Test
    public void initializeTogglesTheHooks()
    {
        /*
        when(manipulator.isOpen()).thenReturn(false);
        OpenHooks command = new OpenHooks(manipulator);
        command.initialize();
        verify(manipulator).setHooks(true);

        when(manipulator.isOpen()).thenReturn(true);
        command = new OpenHooks(manipulator);
        command.initialize();
        verify(manipulator).setHooks(false);
        */
    }
}