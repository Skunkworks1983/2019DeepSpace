package frc.team1983.commands.manipulator;

import frc.team1983.subsystems.Manipulator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_ExtendManipulator
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
        ExtendManipulator command = new ExtendManipulator(manipulator, true);
        command.initialize();
        verify(manipulator).setExtender(true);

        command = new ExtendManipulator(manipulator, false);
        command.initialize();
        verify(manipulator).setExtender(false);
    }

    @Test
    public void initializeTogglesTheExtender()
    {
        /*
        when(manipulator.isExtended()).thenReturn(false);
        ExtendManipulator command = new ExtendManipulator(manipulator);
        command.initialize();
        verify(manipulator).setExtender(true);

        when(manipulator.isExtended()).thenReturn(true);
        command = new ExtendManipulator(manipulator);
        command.initialize();
        verify(manipulator).setExtender(false);
        */
    }
}