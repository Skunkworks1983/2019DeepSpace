package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

/**
 * The elevator uses inches. Anywhere where 'height' is mentioned used the height of the carriage,
 * so the distance traveled by the first stage times three.
 */
public class Elevator extends Subsystem
{
    //setpoint for bottom of the elevator
    public static final int BOTTOM = 0;

    //Setpoints for hatches
    public static final int BOTTOM_HATCH = 0;
    public static final int MIDDLE_HATCH = 0;
    public static final int TOP_HATCH = 0;

    //Setpoints for balls
    public static final int LOW_BALL = 0;
    public static final int MIDDLE_BALL = 0;
    public static final int TOP_BALL = 0;
    public static final int CARGOSHIP_BALL =0;

    public static final double kG = 0.05; //Tested on practice bot with full battery
    public static final double INCHES_PER_TICK = (22.0 * 3.0) / 95.0; // TODO: add math

    public MotorGroup motorGroup;

    public Elevator()
    {
        motorGroup = new MotorGroup("Left Elevator", FeedbackType.POSITION,
                new Spark(RobotMap.Elevator.LEFT, RobotMap.Elevator.LEFT_REVERSED),
                new Spark(RobotMap.Elevator.RIGHT, RobotMap.Elevator.RIGHT_REVERSED)
        );

        motorGroup.setConversionRatio(INCHES_PER_TICK);

        motorGroup.setMovementAcceleration(6);
        motorGroup.setCruiseVelocity(6);
        motorGroup.setKP(0.18); // TODO: add values

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

    public double getPosition()
    {
        return motorGroup.getPositionTicks() * INCHES_PER_TICK;
    }

    public double getVelocity()
    {
        return motorGroup.getVelocity();
    }

    public void setBrake(boolean brake)
    {
        motorGroup.setBrake(brake);
    }
}
