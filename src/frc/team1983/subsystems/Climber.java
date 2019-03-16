package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Talon;

public class Climber extends Subsystem
{
    public MotorGroup motorGroup;

    public static final double INCHES_PER_TICK = 1.0 / 1383.7;

    public Climber()
    {
        motorGroup = new MotorGroup("Climber",
                new Talon(RobotMap.Climber.RIGHT1, RobotMap.Climber.RIGHT1_REVERSED),
                new Talon(RobotMap.Climber.Right2, RobotMap.Climber.RIGHT2_REVERSED));

        motorGroup.setConversionRatio(INCHES_PER_TICK);
        motorGroup.setKP(0.15);

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
