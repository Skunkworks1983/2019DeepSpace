package frc.team1983.autonomous.paths;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class RightLoading extends CommandGroup
{
    public RightLoading()
    {
        addSequential(new DrivePath(Path.RIGHT_LOADING_STATION_LINE_UP_TO_DRIVER_SWITCH, 8));
    }
}
