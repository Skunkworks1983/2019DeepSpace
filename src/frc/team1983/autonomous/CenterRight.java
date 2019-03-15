package frc.team1983.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1983.Robot;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

public class CenterRight extends CommandGroup
{
    public CenterRight()
    {
        Robot.getInstance().getEstimator().setPose(new Pose(13.5, 5.54, 90));
        addSequential(new DrivePath(new Path(
                new Pose(13.5, 5.54, 90),
                new Pose(14.42, 16.74, 90)
        ), 3));
    }
}
