package frc.team1983.autonomous.paths;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.utilities.pathing.Path;

public class RightLoadingToRocketFar extends CommandGroup
{
    public RightLoadingToRocketFar()
    {
        addSequential(new DrivePath(Path.RIGHT_LOADING_STATION_TO_ROCKET_FAR_LINE_UP, 8));
        addSequential(new DrivePath(Path.RIGHT_ROCKET_FAR_LINE_UP_TO_DRIVER_SWITCH, 5));
    }
}
