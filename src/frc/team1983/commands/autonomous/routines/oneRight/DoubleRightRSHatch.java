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
Starting backwards on the right side, this auto places two hatches on the top position of the right side rocket.
 */

public class DoubleRightRSHatch extends Routine
{
    public DoubleRightRSHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(24.72, 21.64, -61.25),
                new Pose(22.62, 23.89, -90)
        ), -0.8));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));

        addSequential(new DrivePath(new Path(
                new Pose(22.62, 23.89, -90),
                (Pose.RIGHT_ROCKET_FAR)
        ), 0.3));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));
        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));

        addSequential(new DrivePath(new Path(
                new Pose(22.28, 23.89, -90),
                (Pose.RIGHT_LOADING_STATION)
        ), 3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.3, true));

        addSequential(new DrivePath(new Path(
                new Pose(25.1, 1.54, 90),
                (Pose.RIGHT_ROCKET_CLOSE)
        ), -3));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));
        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));

    }
}
