package frc.team1983.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class LeftRocketReversed extends CommandGroup
{
    public LeftRocketReversed()
    {
        addSequential(new SetElevatorPosition(Elevator.Setpoints.Panel.ROCKET_BOTTOM));
        addSequential(new DrivePath(new Path(true,
                Pose.LEVEL_1_LEFT_REVERSED,
                Pose.LEFT_ROCKET_FAR.translateRelative(-1, 0)
        ), 12));
        addSequential(new DrivePath(new Path(
                Pose.LEFT_ROCKET_FAR.translateRelative(-1, 0),
                Pose.LEFT_ROCKET_FAR
        ), 5));
        addParallel(new SetManipulatorRollerSpeed(1, 0.25));
        addSequential(new DrivePath(new Path(true,
                Pose.LEFT_ROCKET_FAR,
                Pose.LEFT_ROCKET_FAR.translateRelative(-1, 0)
        ), 5));
    }
}
