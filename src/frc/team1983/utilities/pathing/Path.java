package frc.team1983.utilities.pathing;

import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;

public class Path extends Bezier
{
    public static final double TANGENT_LENGTH = 2.0; // feet

    private Bezier[] path;

    public Path(Pose... poses)
    {
        path = new Bezier[poses.length - 1];

        for(int i = 0; i < poses.length - 1; i++)
        {
            Vector2 position1 = poses[i].getPosition();
            double theta1 = Math.atan2(poses[i].getDirection().getY(), poses[i].getDirection().getX());

            Vector2 position2 = poses[i + 1].getPosition();
            double theta2 = Math.atan2(poses[i + 1].getDirection().getY(), poses[i + 1].getDirection().getX());

            path[i] = new Bezier(
                position1,
                new Vector2(position1.getX() + Math.cos(theta1) * TANGENT_LENGTH, position1.getY() + Math.sin(theta1) * TANGENT_LENGTH),
                new Vector2(position2.getX() + Math.cos(theta2) * -TANGENT_LENGTH, position2.getY() + Math.sin(theta2) * -TANGENT_LENGTH),
                position2
            );
        }
    }

    private Bezier getSegment(double t)
    {
        double length = 0;
        for(Bezier curve : path)
            length += curve.getLength();
        length *= t;
        for(Bezier curve : path)
        {
            if(curve.getLength() <= length)
                return curve;
            length -= curve.getLength();
        }
        return path[path.length - 1];
    }

    public Vector2 evaluate(double t)
    {
        return getSegment(t).evaluate(t);
    }
}
