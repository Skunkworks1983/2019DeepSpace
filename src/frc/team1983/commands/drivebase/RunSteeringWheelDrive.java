package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.services.OI;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.utilities.motors.ControlMode;

/**
 To understand this code, lets do some algebra. If any of this is confusing, or you want a proof of
 why all points on a record have the same centripetal acceleration, ask anyone who has taken AP Physics 1.
 The formula for centripetal acceleration is (linear velocity ^ 2) / radius.
 ________
 | Robot |
 left|       |right
 |   *---------------* < Instantaneous center of curvature
 |       |   ^
 |_______|radius (r)
 ^
 width(w)

 Left wheel velocity = Vl
 Right wheel velocity = Vr
 Average robot velocity = (Vl + Vr) / 2 = Vc

 All points on the robot have the same centripetal acceleration (think of it like a spinning record), so:
 (Vl ^ 2) / (radius + w/2) = (Vc ^ 2) / r = (Vr ^ 2) / (radius - w/2)

 Hopefully it is now clear how we can calculate the velocity of any wheel given the radius and another velocity.
 We use this to calculate the inner and outer velocities, and even the maxiumum possible outer velocity for
 a given radius.
 */

// Currently does not work
public class RunSteeringWheelDrive extends Command
{
    private Drivebase drivebase;
    private OI oi;
    private static final double MAX_VELOCITY = 1000;
    private static final double HALF_TRACK_WIDTH = Drivebase.TRACK_WIDTH / 2;



    public RunSteeringWheelDrive()
    {
        drivebase = Robot.getInstance().getDrivebase();
        oi = Robot.getInstance().getOI();
        requires(drivebase);
    }


    @Override
    public void execute()
    {
        // Rotation of the wheel, determines radius of turn. Assuming positive is to the right.
        double wheelInput = oi.getRightX();
        // Target velocity of the robot
        double throttle = oi.getRightY();

        // If we don't want to turn at all set both sides to the target velocity
        if (wheelInput == 0)
        {
            drivebase.set(ControlMode.Velocity, throttle * MAX_VELOCITY, throttle * MAX_VELOCITY);
            return;
        }

        // Radius is between 0 (spin in place) and infinity (no rotation)
        double radius = Math.abs(1 / wheelInput) - 1;

        // The center velocity is given by the throttle, but may also have to be less than that, because we may be
        //  be limited by the outer wheel velocity.
        double centerVelocity = Math.min(throttle * MAX_VELOCITY,
                Math.sqrt(MAX_VELOCITY * MAX_VELOCITY * radius / (radius + HALF_TRACK_WIDTH)));

        // If the ICC is inside the robot, we have to have a negative velocity and calculate the radius differently
        double innerVelocity;
        if(radius < HALF_TRACK_WIDTH)
            innerVelocity = -Math.sqrt(centerVelocity * centerVelocity * (HALF_TRACK_WIDTH - radius) / radius);
        else
            innerVelocity = Math.sqrt(centerVelocity * centerVelocity * (radius - HALF_TRACK_WIDTH) / radius);

        double outerVelocity = Math.sqrt(centerVelocity * centerVelocity * (radius + HALF_TRACK_WIDTH) / radius);
        
        if(wheelInput > 0)
            drivebase.set(ControlMode.Velocity,
                    outerVelocity * Math.signum(throttle), innerVelocity* Math.signum(throttle));
        else
            drivebase.set(ControlMode.Velocity,
                    innerVelocity * Math.signum(throttle), outerVelocity* Math.signum(throttle));
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
