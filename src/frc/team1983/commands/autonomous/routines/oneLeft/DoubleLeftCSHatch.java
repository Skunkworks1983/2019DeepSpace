package frc.team1983.commands.autonomous.routines.oneLeft;

import frc.team1983.Robot;
import frc.team1983.commands.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class DoubleLeftCSHatch extends Routine
{
    public DoubleLeftCSHatch()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();

        addSequential(new DrivePath(new Path(
                (Pose.LEVEL_1_LEFT),
                (Pose.CARGO_SHIP_LEFT_CLOSE)
        ), 0.8));

        addSequential(new SetManipulatorRollerSpeed(manipulator, 0.5, true));

        addSequential(new DrivePath(new Path(
                (Pose.CARGO_SHIP_LEFT_CLOSE),
                new Pose(9, 21.94, -90)
        ), 0.3));

        addSequential(new DrivePath(new Path(
                new Pose(9, 21.94, -90),
                (Pose.LEFT_LOADING_STATION)
        ), 0.8));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.5, true));

        addSequential(new DrivePath(new Path(
                (Pose.LEFT_LOADING_STATION),
                new Pose(6.81, 23.69, -90)
        ), -0.8));

        addSequential(new DrivePath(new Path(
                new Pose(6.81, 23.69, -90),
                (Pose.CARGO_SHIP_LEFT_MIDDLE)
        ), 0.5));

        addSequential(new SetManipulatorRollerSpeed(manipulator, 0.5, true));


    }
}
