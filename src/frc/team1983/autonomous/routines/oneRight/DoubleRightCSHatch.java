package frc.team1983.autonomous.routines.oneRight;

import frc.team1983.Robot;
import frc.team1983.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.manipulator.SetHooksOpen;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class DoubleHatchCS extends Routine
{
    public DoubleHatchCS()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(17.33, 5.54, 90),
                new Pose(17.36, 21.94, -180)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, false));

        addSequential(new DrivePath(new Path(
                new Pose(17.36, 21.94, -90),
                new Pose(25.1, 1.54, -90)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(25.1, 1.54, 100),
                new Pose(17.36, 23.61, -180)
        ), 3));

        addSequential(new SetHooksOpen(manipulator, false));
    }
}
