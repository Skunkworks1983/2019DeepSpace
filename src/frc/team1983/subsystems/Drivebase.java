package frc.team1983.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.constants.MotorMap;
import frc.team1983.utilities.control.Transmission;

public class Drivebase extends Subsystem
{
    public static final double FEET_PER_TICK = (6.0 * Math.PI / 12.0) / 1360.0; // feet / tick of wheel encoders
    public static final double MAX_VELOCITY = 14.0; // feet / second, empirically measured maximum drive velocity in a straight line
    public static final double TRACK_WIDTH = 26.0 / 12.0; // feet, horizontal distance between left and right wheels

    private Transmission left, right;

    public Drivebase()
    {
        WPI_TalonSRX left1 = new WPI_TalonSRX(MotorMap.Drivebase.LEFT_1);
        left1.setInverted(MotorMap.Drivebase.LEFT_1_REVERSED);
        left1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        WPI_TalonSRX left2 = new WPI_TalonSRX(MotorMap.Drivebase.LEFT_2);
        left2.setInverted(MotorMap.Drivebase.LEFT_2_REVERSED);
        left2.follow(left1);
        WPI_TalonSRX left3 = new WPI_TalonSRX(MotorMap.Drivebase.LEFT_3);
        left3.setInverted(MotorMap.Drivebase.LEFT_3_REVERSED);
        left3.follow(left2);

        WPI_TalonSRX right1 = new WPI_TalonSRX(MotorMap.Drivebase.RIGHT_1);
        right1.setInverted(MotorMap.Drivebase.RIGHT_1_REVERSED);
        right1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        WPI_TalonSRX right2 = new WPI_TalonSRX(MotorMap.Drivebase.RIGHT_2);
        right2.setInverted(MotorMap.Drivebase.RIGHT_2_REVERSED);
        right2.follow(right1);
        WPI_TalonSRX right3 = new WPI_TalonSRX(MotorMap.Drivebase.RIGHT_3);
        right3.setInverted(MotorMap.Drivebase.RIGHT_3_REVERSED);
        right3.follow(right2);

        left = new Transmission(left1, left2, left3);
        right = new Transmission(right1, right2, right3);

        zero();
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new RunTankDrive());
    }

    @Override
    public void periodic()
    {

    }

    public void zero()
    {
        left.setSelectedSensorPosition(0);
        right.setSelectedSensorPosition(0);
    }

    public TalonSRX getPigeonTalon()
    {
        return left3;
    }

    public void setLeft(ControlMode mode, double value)
    {
        left1.set(mode, value);
    }

    public void setRight(ControlMode mode, double value)
    {
        right1.set(mode, value);
    }

    public static double toFeet(double ticks)
    {
        return -ticks * FEET_PER_TICK;
    }

    public static double toTicks(double feet)
    {
        return -feet / FEET_PER_TICK;
    }

    public double getLeftTicks()
    {
        return left1.getSelectedSensorPosition();
    }

    public double getRightTicks()
    {
        return right1.getSelectedSensorPosition();
    }

    public double getLeftPosition()
    {
        return toFeet(getLeftTicks());
    }

    public double getRightPosition()
    {
        return toFeet(getRightTicks());
    }

    public double getLeftVelocity()
    {
        // multiplied by 10 because getSelectedSensorVelocity returns u/100ms
        return toFeet(left1.getSelectedSensorVelocity() * 10);
    }

    public double getRightVelocity()
    {
        // multiplied by 10 because getSelectedSensorVelocity returns u/100ms
        return toFeet(right1.getSelectedSensorVelocity() * 10);
    }

    public void setBrake(boolean brake)
    {
        TalonSRX[] talons = {left1, left2, left3, right1, right2, right3};

        for(TalonSRX talon : talons)
            talon.setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
    }
}
