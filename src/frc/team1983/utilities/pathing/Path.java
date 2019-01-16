package frc.team1983.utilities.pathing;

import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;

public class Path extends Bezier
{
    public static final double TANGENT_LENGTH = 2.0; // Feet

    private Bezier[] path;
    private double length = 0;

    public Path(Pose... poses)
    {
        path = new Bezier[poses.length - 1];

        for(int i = 0; i < poses.length - 1; i++)
        {
            Vector2 position0 = poses[i].getPosition();
            double theta0 = Math.atan2(poses[i].getDirection().getY(), poses[i].getDirection().getX());

            Vector2 position1 = poses[i + 1].getPosition();
            double theta1 = Math.atan2(poses[i + 1].getDirection().getY(), poses[i + 1].getDirection().getX());

            path[i] = new Bezier(
                position0,
                new Vector2(position0.getX() + Math.cos(theta0) * TANGENT_LENGTH, position0.getY() + Math.sin(theta0) * TANGENT_LENGTH),
                new Vector2(position1.getX() + Math.cos(theta1) * -TANGENT_LENGTH, position1.getY() + Math.sin(theta1) * -TANGENT_LENGTH),
                position1
            );
        }
    }

    private Bezier getSegment(double t)
    {
        if (t < 0 || t > 1)
            throw new IllegalArgumentException("Cannot evaluate at " + t + ", must be between 0 and 1!");

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

    public Vector2 evaluateTangent(double t)
    {
        return getSegment(t).evaluateTangent(t);
    }

    @Override
    public double getLength()
    {
        if(length == 0)
            for(int i = 0; i < RESOLUTION * path.length; i++)
                length += evaluate((double) i / RESOLUTION).getDistanceTo(evaluate((double) (i + 1) / RESOLUTION));
        return length;
    }

    public double evaluateClosestT(double resolution, Vector2 point)
    {
        double t = Double.NaN;
        Vector2 test;
        double smallestDistance = Double.MAX_VALUE;

        // Loop through points on the path
        for (int i = 0; i <= resolution; i++)
        {
            double step = Math.min(i * (1.0 / resolution), 1.0);
            test = evaluate(step);

            // Find the distance to the curve
            double dist = point.getDistanceTo(test);

            // If the shortest distance doesn't exist yet, set it to this distance
            if (dist < smallestDistance)
            {
                t = step;
                smallestDistance = dist;
            }
        }

        return t;
    }

    public double evaluateClosestT(Vector2 point)
    {
        return evaluateClosestT(RESOLUTION * path.length, point);
    }
}
