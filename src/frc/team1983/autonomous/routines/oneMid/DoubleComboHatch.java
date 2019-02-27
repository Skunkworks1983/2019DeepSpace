package frc.team1983.autonomous.routines.oneMid;

import frc.team1983.Robot;
import frc.team1983.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetHooksOpen;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class DoubleComboHatch extends Routine
{
    public DoubleComboHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetHooksOpen(manipulator, false)); //TODO: DETERMINE HATCH STABILITY IN 'OPEN'
        addSequential(new SetElevatorPosition(elevator, 0)); //TODO: ADD ACTUAL SETPOINT

        addSequential(new DrivePath(new Path(
                new Pose(13.5, 5.54, 90),
                new Pose(14.42, 16.74, 90)
        ), 3));

        addSequential(new DrivePath(new Path(
                new Pose(14.42, 15.71, -45),
                new Pose(25.1, 1.54, -90)
        ),3));

        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(25.1, 1.54, 54.0),
                new Pose(24.72, 16.36, 61.25)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, false));
    }
}
