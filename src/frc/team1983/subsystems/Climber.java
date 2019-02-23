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
    private Talon climber;

    public Climber()
    {
        climber = new Talon(RobotMap.Climber.RIGHT, RobotMap.Climber.RIGHT_REVERSED);
        motorGroup = new MotorGroup("Climber", FeedbackType.POSITION,
                new Talon(RobotMap.Climber.RIGHT, RobotMap.Climber.RIGHT_REVERSED));
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
