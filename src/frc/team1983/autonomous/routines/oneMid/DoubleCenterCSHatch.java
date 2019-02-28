package frc.team1983.autonomous.routines.oneMid;

import frc.team1983.Robot;
import frc.team1983.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetHooksOpen;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class DoubleCenterCSHatch extends Routine
{
    public DoubleCenterCSHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetHooksOpen(manipulator, true));
        addSequential(new SetElevatorPosition(elevator, /*placeholder*/));

        addSequential(new DrivePath(new Path(
                (Pose.LEVEL_1_MIDDLE),
                (Pose.CARGO_SHIP_MIDDLE_RIGHT)
        ),3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));

        addSequential(new DrivePath(new Path(
                new Pose(14.42, 16.0, -90),
                (Pose.RIGHT_LOADING_STATION)
        ), 3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.3, true));

        addSequential(new DrivePath(new Path(
                new Pose(25.1, 1.54, 90),
                new Pose(16.69, 10.65, -180),
                (Pose.CARGO_SHIP_MIDDLE_LEFT)
        ), 3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
    }
}
