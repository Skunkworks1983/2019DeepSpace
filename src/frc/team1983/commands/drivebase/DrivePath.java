package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.StateEstimator;
import frc.team1983.subsystems.Drivebase;
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
    protected boolean isFinished()
    {
        return false;
    }
}
