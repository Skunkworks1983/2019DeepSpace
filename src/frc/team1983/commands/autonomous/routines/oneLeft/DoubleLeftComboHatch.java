package frc.team1983.commands.autonomous.routines.oneLeft;

import frc.team1983.Robot;
import frc.team1983.commands.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetHooksOpen;
import frc.team1983.commands.manipulator.SetManipulatorExtended;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

/*
This auto places one hatch on the closest hatch position on the left side of the cargo ship, and one additional hatch on
the left side rocket's top position.
 */

public class DoubleLeftComboHatch extends Routine
{
    public DoubleLeftComboHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();


        addSequential(new SetHooksOpen(manipulator, true));
        addSequential(new SetElevatorPosition(elevator, elevator.BOTTOM_HATCH));

        addSequential(new DrivePath(new Path(
                (Pose.LEVEL_1_LEFT),
                (Pose.CARGO_SHIP_LEFT_CLOSE)
        ), 0.8));

        addSequential(new SetHooksOpen(manipulator, false));
        addSequential(new SetManipulatorRollerSpeed(manipulator, 0.5, true));

        addSequential(new DrivePath(new Path(
                (Pose.CARGO_SHIP_LEFT_CLOSE),
                new Pose(9.0, 21.94, -90)
        ), 0.4));

        addSequential(new DrivePath(new Path(
                new Pose(9.0, 21.94, -90),
                (Pose.LEFT_LOADING_STATION)
        ), 0.8));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.5, true));

        addSequential(new DrivePath(new Path(
                (Pose.LEFT_LOADING_STATION),
                new Pose(6.08, 23.45, -90)
        ), -0.8));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.5, true));

        addSequential(new DrivePath(new Path(
                new Pose(6.08, 23.45, -90),
                (Pose.LEFT_ROCKET_FAR)
        ), 0.3));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));
        addSequential(new SetManipulatorRollerSpeed(manipulator, 0.5, true));
        addSequential(new SetManipulatorExtended(manipulator, true));

    }
}