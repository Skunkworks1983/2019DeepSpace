package frc.team1983.commands.manipulator;

import frc.team1983.subsystems.Manipulator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_SetHooksOpen
{
    @Mock
    Manipulator manipulator;

    @Before
    public void setup()
    {
        initMocks(this);
    }

    @Test
    public void initializeSetsTheHooks() throws Exception
    {
        /*
        SetHooksOpen command = new SetHooksOpen(manipulator, true);
        Whitebox.invokeMethod(command, "initialize");

        verify(manipulator).setHooks(true);
        */
    }

    @Test
    public void initializeTogglesTheHooks()
    {
        /*
        when(manipulator.isOpen()).thenReturn(false);
        SetHooksOpen command = new SetHooksOpen(manipulator);
        command.initialize();
        verify(manipulator).setHooks(true);

        when(manipulator.isOpen()).thenReturn(true);
        command = new SetHooksOpen(manipulator);
        command.initialize();
        verify(manipulator).setHooks(false);
        */
    }
}