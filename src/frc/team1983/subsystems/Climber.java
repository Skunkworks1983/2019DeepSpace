package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Talon;

public class Climber extends Subsystem
{
    private MotorGroup motorGroup;

    public static final double TICKS_PER_INCH = 0; //TODO: add math

    public Climber()
    {
        motorGroup = new MotorGroup("Climber", FeedbackType.POSITION,
                new Talon(RobotMap.Climber.RIGHT, RobotMap.Climber.RIGHT_REVERSED));
        motorGroup.setMovementAcceleration(3);
        motorGroup.setMovementVelocity(3);
        motorGroup.setPID(0.01, 0, 0);
    }

    protected void zero()
    {
        motorGroup.zero();
    }

    public void set(ControlMode mode, double value)
    {
        motorGroup.set(mode, value);
    }

    protected double getPosition()
    {
      return motorGroup.getPositionTicks() / TICKS_PER_INCH;
    }

    @Override
    public void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {

    }

    public void setThrottle(double throttle)
    {
        motorGroup.set(ControlMode.Throttle, throttle);
    }
}
