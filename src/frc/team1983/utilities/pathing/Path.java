package frc.team1983.utilities.pathing;

import frc.team1983.Constants;
import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;

public class Path
{
    private Pose[] poses;
    private Bezier[] path;

    public Path(Pose... poses)
    {
        this.poses = poses;
        path = new Bezier[poses.length - 1];

        for(int i = 0; i < poses.length - 1; i++)
        {
            Pose current = poses[i];
            double theta1 = Math.atan2(current.getDirection().getY(), current.getDirection().getX());
            Pose next = poses[i + 1];
            double theta2 = Math.atan2(next.getDirection().getY(), next.getDirection().getX());

            path[i] = new Bezier(
                    current.getPosition(),
                    Vector2.add(current.getPosition(), Vector2.scale(new Vector2(Math.cos(theta1), Math.sin(theta1)), Constants.PATHING_TANGENT_LENGTH)),
                    Vector2.add(next.getPosition(), Vector2.scale(new Vector2(Math.cos(theta2), Math.sin(theta2)), -Constants.PATHING_TANGENT_LENGTH)),
                    next.getPosition()
            );
        }
    }
}
