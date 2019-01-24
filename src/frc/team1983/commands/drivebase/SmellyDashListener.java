package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

import java.nio.file.Paths;
import java.util.ArrayList;

public class SmellyDashListener extends Command
{
    public SmellyDashListener()
    {
        SmartDashboard.putBoolean("gotPath", false);
    }

    @Override
    public void execute()
    {
        String pathString;
        if(SmartDashboard.getBoolean("gotPath", false))
        {
            pathString = SmartDashboard.getString("path", "0,0,0:0,0,0");

            ArrayList<Pose> poses = new ArrayList<>();

            for (String poseString : pathString.split(":"))
            {
                String[] coords = poseString.split(",");

                poses.add(new Pose(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]),
                        Double.parseDouble(coords[2])));
            }

            // The Pose[]::new thing is required for toArray to return an array of Poses, not Objects
            new DrivePath(new Path(poses.toArray(Pose[]::new)), 4);
        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
