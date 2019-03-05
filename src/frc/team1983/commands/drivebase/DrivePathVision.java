package frc.team1983.commands.drivebase;

import frc.team1983.Robot;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.math.Vector2;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.sensors.Limelight;

public class DrivePathVision extends DrivePath
{
    public static final double ACTIVATION_DISTANCE = 3.0;

    private Limelight limelight;

    public DrivePathVision(Drivebase drivebase, StateEstimator estimator, Path path, double velocity, Limelight limelight)
    {
        super(drivebase, estimator, path, velocity);
        this.limelight = limelight;
    }

    public DrivePathVision(Drivebase drivebase, StateEstimator estimator, Path path, double velocity)
    {
        this(drivebase, estimator, path, velocity, Robot.getInstance().getLimelight());
    }

    @Override
    protected void execute()
    {
        if(Vector2.getDistance(estimator.getPosition(), path.evaluate(1.0)) < ACTIVATION_DISTANCE && limelight.isTargetDetected())
            estimator.setPosition(limelight, path.evaluatePose(1.0));
        super.execute();
    }
}
