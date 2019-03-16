package frc.team1983.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.drivebase.DriverSwitch;
import frc.team1983.services.StateEstimator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class RightRocketFarHatch extends CommandGroup
{
    public RightRocketFarHatch(StateEstimator estimator)
    {
        // Reverse path to far rocket and move forward to driver switch position
        addSequential(new DrivePath(Path.REVERSED_LEVEL_1_RIGHT_TO_RIGHT_ROCKET_FAR_LINE_UP, 10));
//        addSequential(new DrivePath(Path.RIGHT_ROCKET_FAR_LINE_UP_TO_DRIVER_SWITCH, 4));
//
//        // Driver hatch placement
//        addSequential(new DriverSwitch());
//
//        // Back up from rocket and drive to loading station
//        addSequential(new DrivePath(Path.RIGHT_ROCKET_FAR_TO_RIGHT_LOADING_STATION_LINE_UP, 4));
//        addSequential(new DrivePath(Path.RIGHT_LOADING_STATION_LINE_UP_TO_RIGHT_LOADING_STATION_DRIVER_SWITCH, 10));
//
//        // Driver hatch pickup
//        addSequential(new DriverSwitch());
//
//        addSequential(new DrivePath(Path.RIGHT_LOADING_STATION_TO_RIGHT_ROCKET_FAR_LINE_UP, 10));
//        addSequential(new DrivePath(Path.RIGHT_ROCKET_FAR_LINE_UP_TO_DRIVER_SWITCH, 3));
//
//
//        // Driver hatch placement
//        addSequential(new DriverSwitch());
    }

    public RightRocketFarHatch()
    {
        this(Robot.getInstance().getEstimator());
    }
}
