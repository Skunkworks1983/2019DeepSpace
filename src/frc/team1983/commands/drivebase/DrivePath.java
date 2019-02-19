package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.Pair;
import frc.team1983.utilities.control.PurePursuitController;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.pathing.Path;

public class DrivePath extends Command
{
    private Drivebase drivebase;
    private StateEstimator estimator;
    private Path path;
    private double velocity;

    public DrivePath(Drivebase drivebase, StateEstimator estimator, Path path, double velocity)
    {
        requires(drivebase);

        this.drivebase = drivebase;
        this.estimator = estimator;
        this.path = path;
        this.velocity = velocity;
    }

    public DrivePath(Path path, double velocity)
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getEstimator(), path, velocity);
    }

    @Override
    public void execute()
    {
        Pair output = PurePursuitController.evaluateOutput(estimator.getCurrentPose(), path, velocity);

        drivebase.setLeft(ControlMode.Throttle, (double) output.getValue1());
        drivebase.setRight(ControlMode.Throttle, (double) output.getValue2());
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    public void end()
    {
        drivebase.setLeft(ControlMode.Throttle, 0);
        drivebase.setRight(ControlMode.Throttle, 0);
    }
}
