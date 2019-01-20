package frc.team1983.utilities.pathing;

import frc.team1983.constants.Constants;
import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;

public class Path
{
    public static final double TANGENT_LENGTH = 2.0; // feet

    protected Bezier[] curves;
    protected double length = 0;

    public Path(Pose... poses)
    {
        curves = new Bezier[poses.length - 1];

        for(int i = 0; i < poses.length - 1; i++)
        {
            Vector2 position0 = poses[i].getPosition();
            double theta0 = Math.toRadians(poses[i].getHeading());

            Vector2 position1 = poses[i + 1].getPosition();
            double theta1 = Math.toRadians(poses[i + 1].getHeading());

            boolean colinear = 1 - Vector2.dot(
                    Vector2.sub(position1, position0).getNormalized(),
                    poses[i].getDirection().getNormalized()
            ) < Constants.EPSILON;

            if(colinear)
                curves[i] = new Bezier(position0, position1);
            else
                curves[i] = new Bezier(
                    position0,
                    new Vector2(position0.getX() + Math.cos(theta0) * TANGENT_LENGTH, position0.getY() + Math.sin(theta0) * TANGENT_LENGTH),
                    new Vector2(position1.getX() + Math.cos(theta1) * -TANGENT_LENGTH, position1.getY() + Math.sin(theta1) * -TANGENT_LENGTH),
                    position1
                );
        }
    }

    public double getLength()
    {
        if(length == 0)
            for(Bezier curve : curves)
                length += curve.getLength();
        return length;
    }

    protected Bezier getCurve(double t)
    {
        double desiredLength = getLength() * t;
        for(Bezier curve : curves)
        {
            if(desiredLength <= curve.getLength())
                return curve;
            desiredLength -= curve.getLength();
        }
        return curves[curves.length - 1];
    }

    protected double evaluateLengthToCurve(Bezier curve)
    {
        double desiredLength = 0;
        for(Bezier pathCurve : curves)
        {
            if(pathCurve.equals(curve))
                return desiredLength;
            desiredLength += pathCurve.getLength();
        }
        return desiredLength;
    }

    protected double evaluateLengthTo(double t)
    {
        double desiredLength = getLength() * t;
        double lengthBehind = 0;
        for(Bezier curve : curves)
        {
            if(desiredLength <= curve.getLength())
                return lengthBehind + desiredLength;
            desiredLength -= curve.getLength();
            lengthBehind += curve.getLength();
        }
        return desiredLength;
    }

    public Vector2 evaluate(double t)
    {
        Bezier curve = getCurve(t);
        return curve.evaluate((evaluateLengthTo(t) - evaluateLengthToCurve(curve)) / curve.getLength());
    }

    public Vector2 evaluateTangent(double t)
    {
        Bezier curve = getCurve(t);
        return curve.evaluateTangent((evaluateLengthTo(t) - evaluateLengthToCurve(curve)) / curve.getLength());
    }

    public Pair evaluateClosestPoint(Vector2 point)
    {
        double closestT = 0;
        Vector2 closest = getCurve(closestT).evaluate(closestT);
        double closestDistance = Vector2.getDistance(closest, point);
        for(double i = 0; i <= Bezier.RESOLUTION * curves.length; i++)
        {
            Vector2 candidate = getCurve(closestT).evaluate(i / Bezier.RESOLUTION);
            double candidateDistance = Vector2.getDistance(candidate, point);
            if(candidateDistance < closestDistance)
            {
                closestT = i / Bezier.RESOLUTION;
                closest = candidate;
                closestDistance = candidateDistance;
            }
        }
        return new Pair(closestT, closest);
    }
}
