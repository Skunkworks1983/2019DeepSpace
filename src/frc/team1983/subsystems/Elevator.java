package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Spark;
import frc.team1983.utilities.motors.Transmission;

public class Elevator extends Subsystem
{
    // TODO: add necessary things

    public static final double TICKS_PER_INCH = 0;//TODO: add math

    public Transmission transmission;

    public Elevator()
    {
        transmission = new Transmission("Left Elevator", FeedbackType.POSITION,
                new Spark(RobotMap.Elevator.LEFT, RobotMap.Elevator.LEFT_REVERSED),
                new Spark(RobotMap.Elevator.RIGHT, RobotMap.Elevator.RIGHT_REVERSED)
        );

        transmission.setTicksPerInch(TICKS_PER_INCH);
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

    public double getPosition()
    {
        return transmission.getPositionInches();
    }

    public double getVelocity()
    {
        return transmission.getVelocityInches();
    }

    public void setBrake(boolean brake)
    {
        transmission.setBrake(brake);
    }
}
