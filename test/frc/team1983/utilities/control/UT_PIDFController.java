package frc.team1983.utilities.control;

import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Transmission;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_PIDFController
{
    @Mock
    Transmission transmission;

    private PIDFController controller;

    @Before
    public void setup()
    {
        initMocks(this);
        when(transmission.pidGet()).thenReturn(0.0);
        when(transmission.getPositionTicks()).thenReturn(0.0);
        controller = new PIDFController(transmission);
        controller.setPID(1,1,1);
    }

    @Test
    public void calculateCanReturnNonZero()
    {
        assertNotEquals(0.0, controller.calculate(1));
    }

    @Test
    public void runsMotionProfiles()
    {
        // Profile just needs to run slowly
        MotionProfile profile = MotionProfile.generateProfile(0, 100,
                1, 1, FeedbackType.POSITION);

        controller.runMotionProfile(profile);
        ArgumentCaptor<Double> argumentCaptor = ArgumentCaptor.forClass(Double.class);
        try {TimeUnit.SECONDS.sleep(1);} catch(Exception e) {assertEquals(0,1);}
        controller.execute();
        verify(transmission).pidWrite(argumentCaptor.capture());
        assertNotEquals(0.0, argumentCaptor.getValue());
    }

    @Test
    public void doesNotAttemptToRunNullMotionProfile()
    {
        controller.runMotionProfile(null);
        controller.execute();
    }

    @Test
    public void doesNotRunWhenNotEnabled()
    {
        controller.disable();
        controller.start();
        verify(transmission, never()).pidWrite(anyDouble());
    }
}