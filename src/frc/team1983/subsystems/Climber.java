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

    public static final double INCHES_PER_TICK = 1.0 / 1383.7;

    public Climber()
    {
        motorGroup = new MotorGroup("Climber", FeedbackType.POSITION,
                new Talon(RobotMap.Climber.RIGHT, RobotMap.Climber.RIGHT_REVERSED));

        motorGroup.setUseMotionProfiles(false);
        motorGroup.setConversionRatio(INCHES_PER_TICK);
        motorGroup.setPID(0.2, 0, 0);

        motorGroup.setBrake(true);
    }

    public void zero()
    {
        motorGroup.zero();
    }

    public void set(ControlMode mode, double value)
    {
        motorGroup.set(mode, value);
    }

    public double getPosition()
    {
        return motorGroup.getPosition();
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
