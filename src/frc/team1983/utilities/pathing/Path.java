package frc.team1983.utilities.pathing;

import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;

public class Path extends Bezier
{
    public static final double TANGENT_LENGTH = 2.0; // feet

    private Bezier[] path;
    private double length = 0;

    public Path(Pose... poses)
    {
        path = new Bezier[poses.length - 1];

        for(int i = 0; i < poses.length - 1; i++)
        {
            Vector2 position0 = poses[i].getPosition();
            double theta0 = Math.toRadians(poses[i].getHeading());

            Vector2 position1 = poses[i + 1].getPosition();
            double theta1 = Math.toRadians(poses[i + 1].getHeading());

            path[i] = new Bezier(
                position0,
                new Vector2(position0.getX() + Math.sin(theta0) * TANGENT_LENGTH, position0.getY() + Math.cos(theta0) * TANGENT_LENGTH),
                new Vector2(position1.getX() + Math.sin(theta1) * -TANGENT_LENGTH, position1.getY() + Math.cos(theta1) * -TANGENT_LENGTH),
                position1
            );
        }
    }

    private Bezier getSegment(double t)
    {
        length = getLength() * t;
        for(Bezier curve : path)
        {
            if(curve.getLength() <= length)
                return curve;
            length -= curve.getLength();
        }
        return path[path.length - 1];
    }

    @Override
    public double getLength()
    {
        if(length == 0)
            for(Bezier curve : path)
                length += curve.getLength();
        return length;
    }

    @Override
    public Vector2 evaluate(double t)
    {
        return getSegment(t).evaluate(t);
    }

    @Override
    public Vector2 approximateClosestPointOnCurve(Vector2 point)
    {
        Vector2 closest = evaluate(0);
        double closestDistance = Vector2.getDistance(closest, point);
        for(int i = 0; i <= RESOLUTION * path.length; i++)
        {
            Vector2 candidate = evaluate((double) i / (RESOLUTION * path.length));
            double candidateDistance = Vector2.getDistance(candidate, point);
            if(candidateDistance < closestDistance)
            {
                closest = candidate;
                closestDistance = candidateDistance;
            }
        }
        return closest;
    }
}
