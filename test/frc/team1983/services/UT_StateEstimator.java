package frc.team1983.services;

import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Pose;
import frc.team1983.utilities.sensors.Gyro;
import frc.team1983.utilities.sensors.Limelight;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UT_StateEstimator
{
    @Mock
    private Drivebase drivebase;

    @Mock
    private Gyro gyro;

    @Mock
    private Limelight limelight;

    StateEstimator estimator;

    @Before
    public void setup()
    {
        initMocks(this);
        estimator = new StateEstimator(drivebase, gyro);
    }

    @Test
    public void displacementTest()
    {
        when(drivebase.getLeftPosition()).thenReturn(1.0);
        when(drivebase.getRightPosition()).thenReturn(1.0);
        when(gyro.getHeading()).thenReturn(90.0);

        estimator.zero();

        estimator.execute();

        assertThat(estimator.getPosition().equals(new Vector2(0, 1)), equalTo(true));
    }

    @Test
    public void setTargetOffsetTest()
    {
        when(limelight.isTargetDetected()).thenReturn(true);
        when(limelight.getXOffset()).thenReturn(3.0);
        when(limelight.getYOffset()).thenReturn(-5.0);

        estimator.zero();
        estimator.setTargetOffset(limelight, new Pose(10, 10, 90));
        assertThat(estimator.getPosition().equals(new Vector2(13, 5)), equalTo(true));

        estimator.zero();
        estimator.setTargetOffset(limelight, new Pose(10, 10, 0));
        assertThat(estimator.getPosition().equals(new Vector2(5, 7)), equalTo(true));

        estimator.zero();
        estimator.setTargetOffset(limelight, new Pose(10, 10, 180));
        assertThat(estimator.getPosition().equals(new Vector2(15, 13)), equalTo(true));

        estimator.zero();
        estimator.setTargetOffset(limelight, new Pose(10, 10, -90));
        assertThat(estimator.getPosition().equals(new Vector2(7, 15)), equalTo(true));
    }
}
