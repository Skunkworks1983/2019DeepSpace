package frc.team1983.commands.autonomous.routines.twoLeft;

import frc.team1983.Robot;
import frc.team1983.commands.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorExtended;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class L2DoubleLeftRSHatch extends Routine {
    public L2DoubleLeftRSHatch() {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();


        addSequential(new DrivePath(new Path(
                new Pose(9.67, 5.54, -90),
                new Pose(4.33, 23.23, -90)
        ), -0.6));

        addSequential(new DrivePath(new Path(
                new Pose(4.33, 23.23, -90),
                (Pose.LEFT_ROCKET_FAR)
        ), 0.4));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));

        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));

        addSequential(new SetElevatorPosition(elevator, elevator.BOTTOM_HATCH));

        addSequential(new DrivePath(new Path(
                (Pose.LEFT_ROCKET_FAR),
                new Pose(4.82, 23.59, -90)
        ), -0.4));

        addSequential(new DrivePath(new Path(
                new Pose(4.82, 23.59, -90),
                (Pose.LEFT_LOADING_STATION)
        ), 3));

        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, -3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));

        addSequential(new DrivePath(new Path(
                new Pose(1.9, 1.54, 90),
                (Pose.LEFT_ROCKET_CLOSE)
        ), 3));

        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));
    }
}