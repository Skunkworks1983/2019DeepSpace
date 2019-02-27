package frc.team1983.autonomous.routines.oneRight;

import frc.team1983.Robot;
import frc.team1983.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetHooksOpen;
import frc.team1983.commands.manipulator.SetManipulatorExtended;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class DoubleRightRSHatch extends Routine
{
    public DoubleRightRSHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetElevatorPosition(elevator, /*setpoint*/));
        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(17.33, 5.54, 90),
                new Pose(14.72, 16.36, 61)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, false));
        addSequential(new SetManipulatorExtended(manipulator, true));
        addSequential(new SetManipulatorExtended(manipulator, false));

        addSequential(new DrivePath(new Path(
                new Pose(24.72, 16.36, -90),
                new Pose(25.1, 1.54, -90)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(24.72, 21.64, -61),
                new Pose(24.72, 21.64, -61.05)
        ), -3));

        addSequential(new SetManipulatorExtended(manipulator, true));

    }
}
