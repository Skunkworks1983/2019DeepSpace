package frc.team1983.utilities.pathing;

import frc.team1983.constants.Constants;
import frc.team1983.utilities.Pair;
import frc.team1983.utilities.math.Bezier;
import frc.team1983.utilities.math.Vector2;

/**
 * Path creates an array of beziers based on given poses.
 * Four points make one cubic bezier, the origin of one pose, two intermediate points, and the origin of the next pose.
 * Intermediate control points for each bezier are determined by a constant tangent length.
 * The tangent length is added on from the origin of pose points in a curve,
 * and are subtracted from the pose of the next curve.
 */
public class Path
{
    public static final double TANGENT_LENGTH = 5.0; // feet

    protected Bezier[] curves;
    protected double length = 0;

    /**
     * Creates bezier curves between given poses
     * If poses are collinear, create a linear bezier
     * Otherwise, generate a cubic bezier starting at the first pose,
     * a control point in the direction of the first pose and TANGENT_LENGTH distance away,
     * a control point in the opposite direction of the next pose and TANGENT_LENGTH distance away,
     * and ending at the next pose.
     * @param poses
     */
    public Path(Pose... poses)
    {
        curves = new Bezier[poses.length - 1];

        for(int i = 0; i < poses.length - 1; i++)
        {
            Vector2 position0 = poses[i].getPosition();
            double theta0 = Math.toRadians(poses[i].getHeading());

            Vector2 position1 = poses[i + 1].getPosition();
            double theta1 = Math.toRadians(poses[i + 1].getHeading());

            boolean collinear = 1 - Vector2.dot(
                    Vector2.sub(position1, position0).getNormalized(),
                    poses[i].getDirection().getNormalized()
            ) < Constants.EPSILON;

            if(collinear)
                curves[i] = new Bezier(position0, position1);
            else
                curves[i] = new Bezier(
                        position0,
                        new Vector2(position0.getX() + Math.cos(theta0) * TANGENT_LENGTH,
                                position0.getY() + Math.sin(theta0) * TANGENT_LENGTH),
                        new Vector2(position1.getX() + Math.cos(theta1) * -TANGENT_LENGTH,
                                position1.getY() + Math.sin(theta1) * -TANGENT_LENGTH),
                        position1);
        }
    }

    /**
     * Sums the length of all curves in this path
     * @return the length
     */
    public double getLength()
    {
        if(length == 0)
            for(Bezier curve : curves)
                length += curve.getLength();
        return length;
    }

    /**
     * Get the index of the curve that a value of t is on
     * @param t the percentage along the curve [0, 1]
     * @return index
     */
    protected int getCurveIndex(double t)
    {
        return (int) Math.min(Math.floor(t * curves.length), curves.length - 1);
    }

    /**
     * Get which curve a certain value of t is on
     * @param t the percentage along the curve [0, 1]
     * @return curve
     */
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

    /**
     * Get the length up until the start of a curve
     * @param curve
     * @return length
     */
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

    /**
     * Get the length up to a value of t
     * @param t the percentage along the curve [0, 1]
     * @return length
     */
    protected double evaluateLengthTo(double t)
    {
        double desiredLength = getLength() * t;
        double lengthBehind = 0;
        for(Bezier curve : curves)
        {
            double curveLength = curve.getLength();
            if(desiredLength <= curveLength)
                return lengthBehind + desiredLength;
            desiredLength -= curveLength;
            lengthBehind += curveLength;
        }
        return lengthBehind + desiredLength;
    }

    /**
     * Evaluate a point on the curve at a value of t
     * @param t the percentage along the curve [0, 1]
     * @return point
     */
    public Vector2 evaluate(double t)
    {
        Bezier curve = getCurve(t);
        return curve.evaluate((evaluateLengthTo(t) - evaluateLengthToCurve(curve)) / curve.getLength());
    }

    /**
     * Evaluate a normalized tangent to the curve at a value of t
     * @param t the percentage along the curve [0, 1]
     * @return normalized vector
     */
    public Vector2 evaluateTangent(double t)
    {
        Bezier curve = getCurve(t);
        return curve.evaluateTangent((evaluateLengthTo(t) - evaluateLengthToCurve(curve)) / curve.getLength());
    }

    /**
     * Evaluate a normalized perpendicular to the curve at a value of t
     * @param t the percentage along the curve [0, 1]
     * @return normalized vector
     */
    public Vector2 evaluateNormal(double t)
    {
        Bezier curve = getCurve(t);
        return curve.evaluateNormal((evaluateLengthTo(t) - evaluateLengthToCurve(curve)) / curve.getLength());
    }

    /**
     * Evaluate the closest point and t of the closest point
     * @param point
     * @return closest point and t of closest point
     */
    public Pair evaluateClosestPoint(Vector2 point)
    {
        double closestT = 0;
        Vector2 closest = getCurve(getCurveIndex(closestT)).evaluate(closestT);
        double closestDistance = Vector2.getDistance(closest, point);
        for(double i = 0; i <= Bezier.RESOLUTION * curves.length; i++)
        {
            double step = i / (Bezier.RESOLUTION * curves.length);
            int index = getCurveIndex(step);
            double bezierT = (step - index / (double) curves.length) * curves.length;

            Vector2 candidate = getCurve(index).evaluate(bezierT);
            double candidateDistance = Vector2.getDistance(candidate, point);
            if(candidateDistance < closestDistance)
            {
                closestT = step;
                closest = candidate;
                closestDistance = candidateDistance;
            }
        }
        return new Pair(closestT, closest);
    }

    /**
     * Test if another object (presumably another Path) is made up of the same Beziers
     * @param o another object to compare to this one
     * @return if the passed object is made up of the same bezier curves as this one
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Path)
        {
            Bezier[] oCurves = ((Path) o).curves;
            if(oCurves.length != curves.length) return false;

            for(int i = 0; i < curves.length; i++)
            {
                if(!curves[i].equals(oCurves[i])) return false;
            }
            return true;
        }
        else return false;
    }
}
