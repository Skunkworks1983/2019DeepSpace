package frc.team1983.commands.autonomous.routines.oneRight;

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

public class DoubleRightComboHatch extends Routine
{
    public DoubleRightComboHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetElevatorPosition(elevator, elevator.BOTTOM_HATCH));
        addSequential(new DrivePath(new Path(
                (Pose.LEVEL_1_RIGHT),
                (Pose.CARGO_SHIP_RIGHT_CLOSE)
        ), 0.6));

        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));

        addSequential(new DrivePath(new Path(
                new Pose(18.0, 21.94, -90),
                (Pose.RIGHT_LOADING_STATION)
        ), 0.7));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.3, true));

        addSequential(new DrivePath(new Path(
                (Pose.RIGHT_LOADING_STATION),
                new Pose(21.75, 23.55, -90)
        ), -0.7));

        addSequential(new SetElevatorPosition(elevator, elevator.TOP_HATCH));

        addSequential(new DrivePath(new Path(
                new Pose(21.75, 23.55, -90),
                (Pose.RIGHT_ROCKET_FAR)
        ), 0.3));

        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
        addSequential(new SetManipulatorExtended(manipulator, false));
    }
}
