package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

public class Elevator extends Subsystem
{
    // TODO: add necessary things

    public static final double kG = 0.05; //Tested on practice bot with full battery
    public static final double TICKS_PER_INCH = 95.0 / (22.0 * 3.0);//TODO: add math

    public MotorGroup motorGroup;

    public Elevator()
    {
        motorGroup = new MotorGroup("Left Elevator", FeedbackType.POSITION,
                new Spark(RobotMap.Elevator.LEFT, RobotMap.Elevator.LEFT_REVERSED),
                new Spark(RobotMap.Elevator.RIGHT, RobotMap.Elevator.RIGHT_REVERSED)
        );

        motorGroup.setMovementAcceleration(12);
        motorGroup.setMovementVelocity(12);
        motorGroup.setPID(0.01, 0, 0); // todo: add values

        motorGroup.setFFOperator(this);
        motorGroup.addFFTerm(Elevator -> kG);

        zero();
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    @Override
    public void periodic()
    {

    }

    public void zero()
    {
        motorGroup.zero();
    }

    public void set(ControlMode mode, double value)
    {
        motorGroup.set(mode, value);
    }

    public double getTargetPosition()
    {
        return motorGroup.getTargetValue() / TICKS_PER_INCH;
    }

    public double getPosition()
    {
        return motorGroup.getPositionTicks() / TICKS_PER_INCH;
    }

    public double getTicks()
    {
        return motorGroup.getPositionTicks();
    }

    public double getVelocity()
    {
        return motorGroup.getVelocityTicks() / TICKS_PER_INCH;
    }

    public void setBrake(boolean brake)
    {
        motorGroup.setBrake(brake);
    }
}
