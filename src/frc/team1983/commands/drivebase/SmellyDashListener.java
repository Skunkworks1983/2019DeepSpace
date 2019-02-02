package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.pathing.Path;
import frc.team1983.utilities.pathing.Pose;

import java.util.ArrayList;

public class SmellyDashListener extends Command
{
    private Logger logger;

    public SmellyDashListener()
    {
        logger = Logger.getInstance();
    }

    @Override
    public void execute()
    {
        if (!SmartDashboard.getBoolean("gotPath", true))
        {
            logger.info("Got a path", getClass()); SmartDashboard.putBoolean("gotPath", true);

            Scheduler.getInstance().add(new DrivePath(constructPathFromString(SmartDashboard.getString("path", "0,0,0:0,0,0")), 4));
        }
    }

    public static Path constructPathFromString(String pathString)
    {
        try
        {
            ArrayList<Pose> poses = new ArrayList<>();

            for (String poseString : pathString.split(":"))
            {
                String[] coords = poseString.split(",");

                poses.add(new Pose(Double.valueOf(coords[0]), Double.valueOf(coords[1]), Double.valueOf(coords[2])));
            }

            for (Pose pose : poses)
                Logger.getInstance().info(pose.getPosition().toString() + pose.getHeading(), SmellyDashListener.class);

            // The Pose[]::new thing is required for toArray to return an array of Poses, not generic Objects
            if (poses.size() > 3) return new Path(poses.get(0), poses.get(1), poses.subList(2, poses.size()).toArray(Pose[]::new));
            if (poses.size() > 2) return new Path(poses.get(0), poses.get(1), poses.get(2));
            return new Path(poses.get(0), poses.get(1));
        } catch(Exception e)
        {
            System.out.println(e);
            return new Path(new Pose(1,1,1), new Pose(1,1,1));
        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
