package frc.team1983.autonomous.paths;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.utilities.pathing.Path;

public class RightLoadingToRocketMiddle extends CommandGroup
{
    public RightLoadingToRocketMiddle()
    {
        addSequential(new DrivePath(Path.RIGHT_LOADING_STATION_TO_ROCKET_MIDDLE_LINE_UP, 8));
        addSequential(new DrivePath(Path.RIGHT_ROCKET_MIDDLE_LINE_UP_TO_DRIVER_SWITCH, 5));
    }
}
