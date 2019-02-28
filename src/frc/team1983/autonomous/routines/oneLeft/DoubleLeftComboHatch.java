package frc.team1983.autonomous.routines.oneLeft;

import frc.team1983.Robot;
import frc.team1983.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.manipulator.SetManipulatorExtended;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.subsystems.Elevator;
import frc.team1983.subsystems.Manipulator;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class DoubleLeftComboHatch extends Routine
{
    public DoubleLeftComboHatch()
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
                new Pose(9.0, 21.94, -90)
        ), 0.4));

        addSequential(new DrivePath(new Path(
                new Pose(9.0, 21.94, -90),
                (Pose.LEFT_LOADING_STATION)
        ), 0.8));

        addSequential(new SetManipulatorRollerSpeed(manipulator, -0.5, true));

        addSequential(new DrivePath(new Path(
                (Pose.LEFT_LOADING_STATION),
                new Pose(6.08, 23.45, -90)
        ), -0.8));

        addSequential(new SetManipulatorExtended(manipulator, true));

        addSequential(new DrivePath(new Path(
                new Pose(6.08, 23.45, -90),
                (Pose.LEFT_ROCKET_FAR)
        ), 0.3));

        addSequential(new SetManipulatorRollerSpeed(manipulator, 0.5, true));
        addSequential(new SetManipulatorExtended(manipulator, false));

    }
}
