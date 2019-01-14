package frc.team1983.utilities.math;

import frc.team1983.constants.Constants;

import java.util.Arrays;

public class Bezier
{
    private Vector2[] points;

    public Bezier(Vector2... points)
    {
        if(points.length < 2)
            throw new IllegalArgumentException("cannot construct Bezier with less than two points");

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

    public static double getLength(Bezier curve)
    {
        double length = 0;
        double segments = 20;
        for(int i = 0; i < segments; i++)
            length += curve.evaluate((double) i / segments).getDistanceTo(curve.evaluate((double) (i + 1) / segments));
        return length;
    }

    public double getLength()
    {
        return getLength(this);
    }
}
