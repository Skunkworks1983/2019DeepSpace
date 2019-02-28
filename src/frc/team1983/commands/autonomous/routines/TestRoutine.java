package frc.team1983.commands.autonomous.routines;

import frc.team1983.commands.autonomous.Routine;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class TestRoutine extends Routine {
    public TestRoutine()
    {
        addSequential(new DrivePath(new Path(
                new Pose(0, 0, 90),
                new Pose(0, 10, 90)
        ), 3));
        addSequential(new SetElevatorPosition(10));
    }
}
