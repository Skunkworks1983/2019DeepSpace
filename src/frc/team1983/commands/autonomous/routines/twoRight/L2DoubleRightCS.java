package frc.team1983.commands.autonomous.routines.twoRight;

import frc.team1983.Robot;
import frc.team1983.commands.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class L2DoubleRightCS extends Routine
{
    public L2DoubleRightCS()
    {
        Elevator elevator = Robot.getInstance().getElevator();
        Manipulator manipulator = Robot.getInstance().getManipulator();


        addSequential(new DrivePath(new Path(
                (Pose.LEVEL_2_RIGHT),
                (Pose.CARGO_SHIP_RIGHT_CLOSE)
        ), 3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));

        addSequential(new DrivePath(new Path(
                (Pose.CARGO_SHIP_RIGHT_CLOSE),
                new Pose(18, 21.94, -90)
        ), -3));

        addSequential(new DrivePath(new Path(
                new Pose(18, 21.94, -90),
                (Pose.RIGHT_LOADING_STATION)
        ), 3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.3, true));

        addSequential(new DrivePath(new Path(
                (Pose.RIGHT_LOADING_STATION),
                new Pose(19.95, 23.5, -90)
        ), -3));

        addSequential(new DrivePath(new Path(
                new Pose(19.95, 23.5, -180),
                (Pose.CARGO_SHIP_RIGHT_MIDDLE)
        ), 3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, .3, true));
    }
}
