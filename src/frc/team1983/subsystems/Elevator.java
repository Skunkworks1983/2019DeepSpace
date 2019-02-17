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

    public static final double TICKS_PER_INCH = 1;//TODO: add math

    public MotorGroup transmission;

    public Elevator()
    {
        transmission = new MotorGroup("Left Elevator", FeedbackType.POSITION,
                new Spark(RobotMap.Elevator.LEFT, RobotMap.Elevator.LEFT_REVERSED),
                new Spark(RobotMap.Elevator.RIGHT, RobotMap.Elevator.RIGHT_REVERSED)
        );

        transmission.setMovementAcceleration(12);
        transmission.setMovementVelocity(12);
        transmission.setPID(0.01, 0, 0); // todo: add values

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
        transmission.zero();
    }

    public void set(ControlMode mode, double value)
    {
        transmission.set(mode, value);
    }

    public double getTargetPosition()
    {
        return transmission.getTargetValue() / TICKS_PER_INCH;
    }

    public double getPosition()
    {
        return transmission.getPositionTicks() / TICKS_PER_INCH;
    }

    public double getVelocity()
    {
        return transmission.getVelocityTicks() / TICKS_PER_INCH;
    }

    public void setBrake(boolean brake)
    {
        transmission.setBrake(brake);
    }
}
