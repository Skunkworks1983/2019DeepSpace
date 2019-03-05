package frc.team1983.commands.autonomous.routines.oneRight;

import frc.team1983.Robot;
import frc.team1983.commands.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

/*
This auto places two hatches on the two closest positions of the cargo ship's right side.
 */

public class DoubleRightCSHatch extends Routine
{
    public DoubleRightCSHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();


        addSequential(new SetElevatorPosition(elevator, elevator.BOTTOM_HATCH));

        addSequential(new DrivePath(new Path(
                (Pose.LEVEL_1_RIGHT),
                (Pose.CARGO_SHIP_RIGHT_CLOSE)
        ), 0.8));

        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));

        addSequential(new DrivePath(new Path(
                (Pose.CARGO_SHIP_RIGHT_CLOSE),
                new Pose(18, 21.94, -90)
        ), -0.3));

        addSequential(new DrivePath(new Path(
                new Pose(18, 21.94, -90),
                (Pose.RIGHT_LOADING_STATION)
        ), 0.8));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.3, true));

        addSequential(new DrivePath(new Path(
                new Pose(25.1, 1.54, 90),
                new Pose(22.77, 10.7, -109.29),
                (Pose.CARGO_SHIP_RIGHT_MIDDLE)
        ), -0.6));

        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
    }
}
