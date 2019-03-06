package frc.team1983.commands.autonomous.routines.oneRight;

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
Starting backwards on the right side, this auto places two hatches on the top far position of the right side rocket.
 */

public class DoubleRightRSHatch extends Routine
{
    public DoubleRightRSHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(17.33, 5.54, -90),
                new Pose(22.52, 23.89, -90)
        ), -3.0));

        addSequential(new DrivePath(new Path(
                new Pose(22.52, 23.89, -90),
                (Pose.RIGHT_ROCKET_FAR)
        ), .5));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));
        addSequential(new SetHooksOpen(manipulator, false));
        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));
        addSequential(new SetElevatorPosition(elevator, elevator.BOTTOM_HATCH));

        addSequential(new DrivePath(new Path(
                new Pose(22.52, 23.89, -90),
                new Pose(21.11, 19.22, -90)
        ), -2.0));

        addSequential(new DrivePath(new Path(
                new Pose(21.11, 19.22, -90),
                (Pose.RIGHT_LOADING_STATION)
        ), 3.0));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.3, true));
        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                (Pose.RIGHT_LOADING_STATION),
                new Pose(22.52, 23.89, -90),
                (Pose.RIGHT_ROCKET_FAR)
        ), -3.0));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));
        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));
        addSequential(new SetElevatorPosition(elevator, 0));

    }
}
