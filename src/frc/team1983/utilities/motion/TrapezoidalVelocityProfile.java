package frc.team1983.utilities.motion;

import frc.team1983.utilities.math.Segment;
import frc.team1983.utilities.math.Vector2;

public class TrapezoidalVelocityProfile extends MotionProfile
{
    public TrapezoidalVelocityProfile(double distance, double velocity, double acceleration)
    {
        super(generate(distance, velocity, acceleration));
    }

    private static Segment[] generate(double distance, double velocity, double acceleration)
    {
        Segment[] segments;

        double accelerationTime = velocity / acceleration;
        double accelerationDisplacement = accelerationTime * velocity;

        if(Math.abs(accelerationDisplacement * 2) > Math.abs(distance))
            throw new IllegalArgumentException("acceleration " + acceleration + " is too high to generate trapezoidal velocity profile");
        if(accelerationDisplacement * 2 == distance)
        {
            segments = new Segment[] {
                    new Segment(new Vector2(0, 0), new Vector2(accelerationTime, velocity)),
                    new Segment(new Vector2(accelerationTime, velocity), new Vector2(accelerationTime * 2, 0))
            };
        }
        else
        {
            double cruiseTime = (distance - accelerationDisplacement) / velocity;
            segments = new Segment[] {
                    new Segment(new Vector2(0, 0), new Vector2(accelerationTime, velocity)),
                    new Segment(new Vector2(accelerationTime, velocity), new Vector2(accelerationTime + cruiseTime, velocity)),
                    new Segment(new Vector2(accelerationTime + cruiseTime, velocity), new Vector2(accelerationTime * 2 + cruiseTime, 0))
            };
        }

        return segments;
    }
}
