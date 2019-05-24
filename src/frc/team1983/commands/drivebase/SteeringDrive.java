package frc.team1983.commands.drivebase;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1983.Robot;
import frc.team1983.utilities.motors.ControlMode;

public class SteeringDrive extends Command
{
    protected static final double DRIVEBASE_WIDTH = 19.75 / 12;
    protected static final double MAX_VELOCITY = 14.0; // f/s
    protected static final double TURNING_CONSTANT = 1.0;

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
        steer = Math.signum(throttle) * Robot.getInstance().getOI().getLeftX();

        double turningCorrection = 2 * (1 - Math.abs(throttle));

        double vl = throttle / turningCorrection, vr = throttle / turningCorrection;

        if(steer == 0)
        {
            Robot.getInstance().getDrivebase().set(ControlMode.Throttle, vl, vr);
            return;
        }

        double r = TURNING_CONSTANT / steer - (Math.signum(steer) * TURNING_CONSTANT);

        vl = (throttle * (r + TURNING_CONSTANT)) / (r * Math.abs(1 - steer)) / turningCorrection;
        vr = (throttle * (r - TURNING_CONSTANT)) / (r * Math.abs(1 - steer)) / turningCorrection;

        Robot.getInstance().getDrivebase().set(ControlMode.Throttle, vl, vr);

        System.out.println(r);
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
