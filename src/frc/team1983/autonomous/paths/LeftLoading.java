package frc.team1983.autonomous.paths;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.utilities.pathing.Path;

public class LeftLoading extends CommandGroup
{
    public LeftLoading()
    {
        addSequential(new DrivePath(Path.LEFT_LOADING_STATION_LINE_UP_TO_DRIVER_SWITCH, 8));
    }
}
