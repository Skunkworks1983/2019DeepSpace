package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Spark;
import frc.team1983.utilities.motors.Transmission;

public class Drivebase extends Subsystem
{
    public static final double TICKS_PER_INCH = 8.69 / (6.0 * Math.PI); // encoder pulses / inches of travel
    public static final double MAX_VELOCITY = 14.0; // feet / second, empirically measured maximum drive velocity in a straight line
    public static final double MAX_ACCELERATION = 3.0; // feet / second / second, a wild guess based on MAX_VELOCITY
    public static final double TRACK_WIDTH = 26.0 / 12.0; // feet, horizontal distance between left and right wheels

    public Transmission left, right;

    public Drivebase()
    {
        left = new Transmission("Left Drivebase", FeedbackType.VELOCITY,
                new Spark(RobotMap.Drivebase.LEFT_1, RobotMap.Drivebase.LEFT_1_REVERSED),
                new Spark(RobotMap.Drivebase.LEFT_2, RobotMap.Drivebase.LEFT_2_REVERSED),
                new Spark(RobotMap.Drivebase.LEFT_3, RobotMap.Drivebase.LEFT_3_REVERSED)
        );

        left.setTicksPerInch(TICKS_PER_INCH);
        left.setMovementVelocity(12 * 3);
        left.setMovementAcceleration(12 * 12);
        left.setPID(0.03, 0 , 0);


        right = new Transmission("Right Drivebase", FeedbackType.VELOCITY,
                new Spark(RobotMap.Drivebase.RIGHT_1, RobotMap.Drivebase.RIGHT_1_REVERSED),
                new Spark(RobotMap.Drivebase.RIGHT_2, RobotMap.Drivebase.RIGHT_2_REVERSED),
                new Spark(RobotMap.Drivebase.RIGHT_3, RobotMap.Drivebase.RIGHT_3_REVERSED)
        );

        right.setTicksPerInch(TICKS_PER_INCH);
        right.setMovementVelocity(12 * 3);
        right.setMovementAcceleration(12 * 12);
        right.setPID(0.03, 0 , 0);


        // todo: configure encoder properties of transmissions

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
        left.zero();
        right.zero();
    }

    public void setLeft(ControlMode mode, double value)
    {
        left.set(mode, value);
    }

    public void setRight(ControlMode mode, double value)
    {
        right.set(mode, value);
    }

    public double getLeftPosition()
    {
        return left.getPositionInches();
    }

    public double getLeftVelocity()
    {
        return left.getVelocityInches();
    }

    public double getRightPosition()
    {
        return right.getPositionInches();
    }

    public double getRightVelocity()
    {
        return right.getVelocityInches();
    }


    public void setBrake(boolean brake)
    {
        left.setBrake(brake);
        right.setBrake(brake);
    }
}
