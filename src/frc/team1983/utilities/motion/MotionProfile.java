package frc.team1983.utilities.motion;

import frc.team1983.utilities.math.Segment;
import frc.team1983.utilities.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

public class MotionProfile
{
    private ArrayList<Segment> segments;
    private double duration;

    public MotionProfile(Segment... segments)
    {
        this.segments = new ArrayList<>();
        this.segments.addAll(Arrays.asList(segments));
        duration = segments[segments.length - 1].getPoint1().getX();
    }

    public double getDuration()
    {
        return duration;
    }

    private Segment getSegment(double time)
    {
        if(time < 0 || time > duration)
            throw new IllegalArgumentException("segment " + time + " is not in domain of profile [0, " + duration + "]");

        for(Segment segment : segments)
            if(segment.getPoint0().getX() <= time && time <= segment.getPoint1().getX())
                return segment;
        return null;
    }

    public double evaluateDisplacement(double time)
    {
        if(time < 0 || time > duration)
            throw new IllegalArgumentException("segment " + time + " is not in domain of profile [0, " + duration + "]");

        double area = 0;

        // trapezoidal sum of all segments to find area
        for(Segment segment : segments)
        {
            double vel_1 = segment.getPoint0().getY();
            double vel_2 = 0, dt = 0;

            // check if we are trying to evaluate a portion of a segment or a whole segment
            if(segment.getPoint1().getX() < time)
            {
                // evaluate the whole segment
                vel_2 = segment.getPoint1().getY();
                dt = (segment.getPoint1().getX() - segment.getPoint0().getX());
            }
            else if(segment.getPoint0().getX() <= time)
            {
                // evaluate a portion of the segment, [start, time]
                vel_2 = segment.evaluate(time - segment.getPoint0().getX()).getY();
                dt = time - segment.getPoint0().getX();
            }

            // area of a trapezoid
            area += ((vel_1 + vel_2) / 2) * dt;
        }

        return area;
    }

    public double evaluateVelocity(double time)
    {
        Segment segment = getSegment(time);
        return segment.evaluate(time - segment.getPoint0().getX()).getY();
    }

    public double evaluateAcceleration(double time)
    {
        Segment segment = getSegment(time);
        Vector2 derivative = segment.evaluateDerivative(time - segment.getPoint0().getX());
        return derivative.getY() / derivative.getX();
    }
}

