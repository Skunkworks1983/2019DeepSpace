package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.Constants;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.MotorGroup;
import frc.team1983.utilities.motors.Spark;

/**
 * Drivebase units are feet
 */
public class Drivebase extends Subsystem
{
    public static final double FEET_PER_TICK = (6.0 * Math.PI / 12.0) / (8.69/* * Spark.SPARK_INTERNAL_ENCODER_RESOLUTION*/); // encoder pulses / feet of travel
    public static final double MAX_VELOCITY = 14.0; // Whatever the sparks return. Empirically measured by Bella driving like a madman.
    public static final double TRACK_WIDTH = (Constants.ROBOT_WIDTH - (3.0 / 12.0)); // feet, horizontal distance between left and right wheels

    public MotorGroup left, right;

    public Drivebase()
    {
        left = new MotorGroup("Left Drivebase",
                new Spark(RobotMap.Drivebase.LEFT_1, RobotMap.Drivebase.LEFT_1_REVERSED),
                new Spark(RobotMap.Drivebase.LEFT_2, RobotMap.Drivebase.LEFT_2_REVERSED),
                new Spark(RobotMap.Drivebase.LEFT_3, RobotMap.Drivebase.LEFT_3_REVERSED)
        );

        left.setConversionRatio(FEET_PER_TICK);

        left.setCruiseVelocity(5.0);
        left.setMovementAcceleration(2.0);
        left.setKP(0.003);
        left.setKD(0.0015);

        right = new MotorGroup("Right Drivebase",
                new Spark(RobotMap.Drivebase.RIGHT_1, RobotMap.Drivebase.RIGHT_1_REVERSED),
                new Spark(RobotMap.Drivebase.RIGHT_2, RobotMap.Drivebase.RIGHT_2_REVERSED),
                new Spark(RobotMap.Drivebase.RIGHT_3, RobotMap.Drivebase.RIGHT_3_REVERSED)
        );

        right.setConversionRatio(FEET_PER_TICK);

        right.setCruiseVelocity(5.0);
        right.setMovementAcceleration(2.0);
        right.setKP(0.003);
        right.setKD(0.0015);

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

    public void set(ControlMode mode, double leftValue, double rightValue)
    {
        setLeft(mode, leftValue);
        setRight(mode, rightValue);
    }

    public void set(ControlMode mode, double value)
    {
        setLeft(mode, value);
        setRight(mode, value);
    }

    public double getLeftPosition()
    {
        return left.getPosition();
    }

    public double getRightPosition()
    {
        return right.getPosition();
    }

    public void setBrake(boolean brake)
    {
        left.setBrake(brake);
        right.setBrake(brake);
    }
}
