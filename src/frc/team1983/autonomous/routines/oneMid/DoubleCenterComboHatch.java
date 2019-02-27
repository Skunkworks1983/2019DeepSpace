package frc.team1983.autonomous.routines.oneMid;

import frc.team1983.Robot;
import frc.team1983.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetHooksOpen;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class DoubleCenterComboHatch extends Routine
{
    public DoubleCenterComboHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetHooksOpen(manipulator, true));
        addSequential(new SetElevatorPosition(elevator, /*placeholder*/));

        addSequential(new DrivePath(new Path(
                new Pose(13.5, 5.54, 90),
                new Pose(14.42, 16.74, 90)
        ),3));

        addSequential(new SetHooksOpen(manipulator, false));

        addSequential(new DrivePath(new Path(
                new Pose(14.42, 16.74, -45),
                new Pose(25.1, 1.54, -90)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, true));
        addSequential(new SetHooksOpen(manipulator, false));

        addSequential(new DrivePath(new Path(
                new Pose(25.1, 1.54, 90),
                new Pose(20.58, 11.48, 146.88),
                new Pose(14.42, 16.74, 90)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, false));
    }
}
