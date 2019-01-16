package frc.team1983.utilities.math;

import frc.team1983.constants.Constants;
import frc.team1983.utilities.Pair;

import java.util.Arrays;

public class Bezier
{
    public static int RESOLUTION = 20;

    private final Vector2[] points;
    private double length = 0;

    public Bezier(Vector2... points)
    {
        if(points.length < 2)
            throw new IllegalArgumentException("Cannot generate a Bezier curve with less than two points");
        this.points = points;
    }

    public static Vector2 evaluate(Bezier curve, double t)
    {
        if(curve.points.length == 2)
            return Vector2.add(curve.points[0], Vector2.scale(Vector2.sub(curve.points[1], curve.points[0]), t));
        else
            return new Bezier(
                    new Bezier(Arrays.copyOfRange(curve.points, 0, curve.points.length - 1)).evaluate(t),
                    new Bezier(Arrays.copyOfRange(curve.points, 1, curve.points.length)).evaluate(t)
            ).evaluate(t);
    }

    public Vector2 evaluate(double t)
    {
        return evaluate(this, t);
    }

    public static double getLength(Bezier curve)
    {
        if(curve.length == 0)
            for(double i = 0; i < RESOLUTION; i++)
                curve.length += curve.evaluate(i / RESOLUTION).getDistanceTo(curve.evaluate((i + 1) / RESOLUTION));
        return curve.length;
    }

    public double getLength()
    {
        return getLength(this);
    }

    public static Vector2 evaluateTangent(Bezier curve, double t)
    {
        if(curve.points.length == 2)
            return Vector2.sub(curve.points[1], curve.points[0]).getNormalized();
        else
            return Vector2.sub(new Bezier(Arrays.copyOfRange(curve.points, 1, curve.points.length)).evaluate(t),
                    new Bezier(Arrays.copyOfRange(curve.points, 0, curve.points.length - 1)).evaluate(t)).getNormalized();
    }

    public Vector2 evaluateTangent(double t)
    {
        return evaluateTangent(this, t);
    }

    public static Vector2 evaluateNormal(Bezier curve, double t)
    {
        Vector2 tangent = curve.evaluateTangent(t);
        return new Vector2(-tangent.getY(), tangent.getX());
    }

    public Vector2 evaluateNormal(double t)
    {
        return evaluateNormal(this, t);
    }

    public static Vector2 offset(Bezier curve, double t, double offset)
    {
        return Vector2.add(curve.evaluate(t), Vector2.scale(curve.evaluateNormal(t), offset));
    }

    public Vector2 offset(double t, double offset)
    {
        return offset(this, t, offset);
    }

    public static Vector2 evaluateCenterOfCurvature(Bezier curve, double t)
    {
        return Line.cast(new Line(curve.evaluate(t - Constants.EPSILON), curve.evaluateNormal(t - Constants.EPSILON)),
                new Line(curve.evaluate(t + Constants.EPSILON), curve.evaluateNormal(t + Constants.EPSILON)));
    }

    public Vector2 evaluateCenterOfCurvature(double t)
    {
        return evaluateCenterOfCurvature(this, t);
    }

    public static double evaluateRadiusOfCurvatuve(Bezier curve, double t)
    {
        return Vector2.getDistance(curve.evaluate(t), curve.evaluateCenterOfCurvature(t));
    }

    public double evaluateRadiusOfCurvatuve(double t)
    {
        return evaluateRadiusOfCurvatuve(this, t);
    }

    public static Pair approximateClosestPointOnCurve(Bezier curve, Vector2 point)
    {
        double closestT = 0;
        Vector2 closest = curve.evaluate(closestT);
        double closestDistance = Vector2.getDistance(closest, point);
        for(double i = 0; i <= RESOLUTION; i++)
        {
            Vector2 candidate = curve.evaluate(i / RESOLUTION);
            double candidateDistance = Vector2.getDistance(candidate, point);
            if(candidateDistance < closestDistance)
            {
                closestT = i / RESOLUTION;
                closest = candidate;
                closestDistance = candidateDistance;
            }
        }
        return new Pair(closestT, closest);
    }

    public Pair approximateClosestPointOnCurve(Vector2 point)
    {
        return approximateClosestPointOnCurve(this, point);
    }
}
