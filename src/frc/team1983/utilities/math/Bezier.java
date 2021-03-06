package frc.team1983.utilities.math;

import frc.team1983.utilities.Pair;
import frc.team1983.utilities.pathing.Pose;

import java.util.Arrays;

/**
 * Bezier is a parametric function that follows a series of control points.
 */
public class Bezier
{
    public static int RESOLUTION = 50;
    public static double CAST_EPSILON = 1e-2;

    protected final Vector2[] points;
    private double length = 0;

    /**
     * Creates a bezier with at least two points
     *
     * @param point0 the first point, must be defined
     * @param point1 the second point, must be defined
     * @param points all other points
     */
    public Bezier(Vector2 point0, Vector2 point1, Vector2... points)
    {
        this.points = new Vector2[points.length + 2];

        this.points[0] = point0; this.points[1] = point1;

        for (int i = 0; i < points.length; i++)
            this.points[i + 2] = points[i];
    }

    /**
     * Evaluate a point on the curve at a value of t
     *
     * @param t the percentage along the curve [0, 1]
     * @return point
     */
    public Vector2 evaluate(double t)
    {
        if (points.length == 2) return Vector2.add(points[0], Vector2.scale(Vector2.sub(points[1], points[0]), t));
        else
            return new Bezier(new Bezier(points[0], points[1], Arrays.copyOfRange(points, 2, points.length - 1)).evaluate(t), new Bezier(points[1], points[2], Arrays.copyOfRange(points, 3, points.length)).evaluate(t)).evaluate(t);
    }

    /**
     * Sums the length of all curves in this path
     *
     * @return the length
     */
    public double getLength()
    {
        if (length == 0) for (double i = 0; i < RESOLUTION; i++)
            length += evaluate(i / RESOLUTION).getDistanceTo(evaluate((i + 1) / RESOLUTION)); return length;
    }

    /**
     * Evaluates the derivative of the curve at a value of t
     *
     * @param t the percentage along the curve [0, 1]
     * @return derivative vector
     */
    public Vector2 evaluateDerivative(double t)
    {
        if (points.length == 2) return Vector2.sub(points[1], points[0]).getNormalized();
        else
            return Vector2.sub(new Bezier(points[1], points[2], Arrays.copyOfRange(points, 3, points.length)).evaluate(t), new Bezier(points[0], points[1], Arrays.copyOfRange(points, 0, points.length - 1)).evaluate(t));
    }

    /**
     * Evaluate a normalized tangent to the curve at a value of t
     *
     * @param t the percentage along the curve [0, 1]
     * @return normalized vector
     */
    public Vector2 evaluateTangent(double t)
    {
        return evaluateDerivative(t).getNormalized();
    }

    /**
     * Evaluate a normalized perpendicular to the curve at a value of t
     *
     * @param t the percentage along the curve [0, 1]
     * @return normalized vector
     */
    public Vector2 evaluateNormal(double t)
    {
        Vector2 tangent = evaluateTangent(t); return new Vector2(-tangent.getY(), tangent.getX());
    }

    /**
     * Evaluate a pose at a value of t
     *
     * @param t the percentage along the curve [0, 1]
     * @return pose
     */
    public Pose evaluatePose(double t)
    {
        return new Pose(evaluate(t), evaluateTangent(t));
    }

    /**
     * Evaluate a point that is offset a distance along the normal at B(t)
     *
     * @param t the percentage along the curve [0, 1]
     * @param offset
     * @return point
     */
    public Vector2 offset(double t, double offset)
    {
        return Vector2.add(evaluate(t), Vector2.scale(evaluateNormal(t), offset));
    }

    /**
     * Evaluates the center of curvature of a point on the curve
     *
     * @param t the percentage along the curve [0, 1]
     * @return center of curvature
     */
    public Vector2 evaluateCenterOfCurvature(double t)
    {
        return Line.cast(new Line(evaluate(t - CAST_EPSILON), evaluateNormal(t - CAST_EPSILON)),
                         new Line(evaluate(t + CAST_EPSILON), evaluateNormal(t + CAST_EPSILON)));
    }

    /**
     * Evaluates the distance of the center of curvature
     *
     * @param t the percentage along the curve [0, 1]
     * @return radius of curvature
     */
    public double evaluateRadiusOfCurvatuve(double t)
    {
        return Vector2.getDistance(evaluate(t), evaluateCenterOfCurvature(t));
    }

    /**
     * Evaluate the closest point and t of the closest point
     *
     * @param point
     * @return closest point and t of closest point
     */
    public Pair<Double, Vector2> evaluateClosestPointAndT(Vector2 point)
    {
        double closestT = 0;
        Vector2 closest = evaluate(closestT);
        double closestDistance = Vector2.getDistance(closest, point);
        for(double i = 0; i <= RESOLUTION; i++)
        {
            Vector2 candidate = evaluate(i / RESOLUTION);
            double candidateDistance = Vector2.getDistance(candidate, point);
            if(candidateDistance < closestDistance)
            {
                closestT = i / RESOLUTION;
                closest = candidate;
                closestDistance = candidateDistance;
            }
        }
        return new Pair<>(closestT, closest);
    }

    /**
     * Evaluate the closest point from another point
     *
     * @param point
     * @return closest point
     */
    public Vector2 evaluateClosestPoint(Vector2 point)
    {
        return (Vector2) evaluateClosestPointAndT(point).getValue2();
    }

    /**
     * Evaluate the closest t from another point
     *
     * @param point
     * @return closest t the percentage along the curve [0, 1]
     */
    public double evaluateClosestT(Vector2 point)
    {
        return (double) evaluateClosestPointAndT(point).getValue1();
    }

    /**
     * Tests if another object (presumable another Bezier) are made up of the same points
     *
     * @param o another object to compare to this object
     * @return if the two beziers share the same point values
     */
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Bezier)
        {
            Vector2[] oPoints = ((Bezier) o).points;
            if (oPoints.length != points.length) return false;

            for (int i = 0; i < points.length; i++)
            {
                if (!points[i].equals(oPoints[i])) return false;
            }

            return true;
        } else return false;
    }
}
