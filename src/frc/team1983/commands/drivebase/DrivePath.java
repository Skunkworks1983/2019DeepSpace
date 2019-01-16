package frc.team1983.commands.drivebase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.control.PurePursuitController;
import frc.team1983.utilities.pathing.Path;

public class DrivePath extends Command
{
    private Drivebase drivebase;
    private StateEstimator estimator;
    private Path path;

    public DrivePath(Drivebase drivebase, StateEstimator estimator, Path path)
    {
        requires(drivebase);

        this.drivebase = drivebase;
        this.estimator = estimator;
        this.path = path;
    }

    public DrivePath(Path path)
    {
        this(Robot.getInstance().getDrivebase(), Robot.getInstance().getEstimator(), path);
    }

    @Override
    public void execute()
    {
        double[] output = PurePursuitController.evaluateOutput(estimator.getCurrentPose(), path, 5);

        drivebase.setLeft(ControlMode.PercentOutput, output[0]);
        drivebase.setLeft(ControlMode.PercentOutput, output[1]);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    public void end()
    {
        drivebase.setLeft(ControlMode.PercentOutput, 0);
        drivebase.setRight(ControlMode.PercentOutput, 0);
    }
}
