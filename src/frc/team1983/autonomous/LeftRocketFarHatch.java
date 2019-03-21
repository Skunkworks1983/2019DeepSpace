package frc.team1983.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.drivebase.DriverSwitch;
import frc.team1983.utilities.pathing.Path;

public class LeftRocketFarHatch extends CommandGroup
{
    public LeftRocketFarHatch()
    {
        // Reverse path to far rocket and move forward to driver switch position
        addSequential(new DrivePath(Path.REVERSED_LEVEL_1_LEFT_TO_ROCKET_FAR_LINE_UP, 10));
        addSequential(new DrivePath(Path.LEFT_ROCKET_FAR_LINE_UP_TO_DRIVER_SWITCH, 5));

        // Driver hatch placement
        addSequential(new DriverSwitch());

        // Back up from rocket and drive to loading station
        addSequential(new DrivePath(Path.LEFT_ROCKET_FAR_TO_LOADING_STATION_LINE_UP, 5), 2);
        addSequential(new DrivePath(Path.LEFT_LOADING_STATION_LINE_UP_TO_DRIVER_SWITCH, 10));

        // Driver hatch pickup
        addSequential(new DriverSwitch());

        addSequential(new DrivePath(Path.LEFT_LOADING_STATION_TO_ROCKET_FAR_LINE_UP, 8));
        addSequential(new DrivePath(Path.LEFT_ROCKET_FAR_LINE_UP_TO_DRIVER_SWITCH, 5));

        // Driver hatch placement
        addSequential(new DriverSwitch());
    }
}
