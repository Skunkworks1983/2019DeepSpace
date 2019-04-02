package frc.team1983.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class RightRocketReversed extends CommandGroup
{
    public RightRocketReversed()
    {
        addSequential(new SetElevatorPosition(Elevator.Setpoints.Panel.ROCKET_BOTTOM));
        addSequential(new DrivePath(new Path(true,
            Pose.LEVEL_1_RIGHT_REVERSED,
            Pose.RIGHT_ROCKET_FAR.translateRelative(-1, 0)
        ), 12));
        addSequential(new DrivePath(new Path(
                Pose.RIGHT_ROCKET_FAR.translateRelative(-1, 0),
                Pose.RIGHT_ROCKET_FAR
        ), 5));
        addParallel(new SetManipulatorRollerSpeed(1, 0.25));
        addSequential(new DrivePath(new Path(true,
                Pose.RIGHT_ROCKET_FAR,
                Pose.RIGHT_ROCKET_FAR.translateRelative(-3, 0)
        ), 5));
    }
}
