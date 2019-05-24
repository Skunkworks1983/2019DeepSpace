package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.utilities.motors.ControlMode;

public class SteeringDrive extends Command
{
    protected static final double MAX_VELOCITY = 14.0; // f/s
    protected static final double STEERING_SCALE = 2.0;
    protected static final double VELOCITY_CORRECTION_SCALE = 2.0;

    private double steer, throttle = 0;

    public SteeringDrive()
    {
        requires(Robot.getInstance().getDrivebase());
    }

    protected void initialize()
    {

    }

    protected void execute()
    {
        throttle = Robot.getInstance().getOI().getRightY();

        // If moving in reverse, change the direction of steering
        // This is opposite of reversing in a car
        steer = Math.signum(throttle) * Robot.getInstance().getOI().getLeftX();

        // Scale the drive train velocities based on the amount of throttle
        // This allows the robot to maintain the same turning radius no matter the throttle
        double velocityCorrection = VELOCITY_CORRECTION_SCALE * Math.abs(throttle);

        double vl = throttle * velocityCorrection;
        double vr = throttle * velocityCorrection;

        // If steer is 0, go straight
        if(steer == 0)
        {
            Robot.getInstance().getDrivebase().set(ControlMode.Throttle, vl, vr);
            return;
        }

        // Determine the turning radius
        double r = STEERING_SCALE / steer - (STEERING_SCALE * Math.signum(steer));

        // If the turning radius is 0, do a point turn
        if(r == 0)
        {
            Robot.getInstance().getDrivebase().set(ControlMode.Throttle, Math.signum(steer) * vl, -Math.signum(steer) * vr);
            return;
        }

        // Determine the wheel velocities from the radius
        vl = (throttle * (r + STEERING_SCALE)) / (r * Math.abs(1 - steer)) * velocityCorrection;
        vr = (throttle * (r - STEERING_SCALE)) / (r * Math.abs(1 - steer)) * velocityCorrection;

        Robot.getInstance().getDrivebase().set(ControlMode.Throttle, vl, vr);
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void interrupted()
    {
        end();
    }

    protected void end()
    {
        Robot.getInstance().getDrivebase().set(ControlMode.Throttle, 0, 0);
    }
}
