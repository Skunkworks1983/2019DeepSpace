package frc.team1983.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.constants.MotorMap;

public class Drivebase extends Subsystem
{
    public static final double FEET_PER_TICK = (6.0 * Math.PI / 12.0) / 1360.0;

    private TalonSRX left1, left2, left3;
    private TalonSRX right1, right2, right3;

    public Drivebase()
    {
        left1 = new TalonSRX(MotorMap.Drivebase.LEFT_1);
        left1.setInverted(MotorMap.Drivebase.LEFT_1_REVERSED);
        left1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        left2 = new TalonSRX(MotorMap.Drivebase.LEFT_2);
        left2.setInverted(MotorMap.Drivebase.LEFT_2_REVERSED);
        left2.follow(left1);
        left3 = new TalonSRX(MotorMap.Drivebase.LEFT_3);
        left3.setInverted(MotorMap.Drivebase.LEFT_3_REVERSED);
        left3.follow(left2);

        right1 = new TalonSRX(MotorMap.Drivebase.RIGHT_1);
        right1.setInverted(MotorMap.Drivebase.RIGHT_1_REVERSED);
        right1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        right2 = new TalonSRX(MotorMap.Drivebase.RIGHT_2);
        right2.setInverted(MotorMap.Drivebase.RIGHT_2_REVERSED);
        right2.follow(right1);
        right3 = new TalonSRX(MotorMap.Drivebase.RIGHT_3);
        right3.setInverted(MotorMap.Drivebase.RIGHT_3_REVERSED);
        right3.follow(right2);
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
        return ticks * FEET_PER_TICK;
    }

    public static double toTicks(double feet)
    {
        return feet / FEET_PER_TICK;
    }

    public double getLeftPosition()
    {
        return toFeet(left1.getSelectedSensorPosition());
    }

    public double getRightPosition()
    {
        return toFeet(right1.getSelectedSensorPosition());
    }

    public double getLeftVelocity()
    {
        return toFeet(left1.getSelectedSensorVelocity() * 10);
    }

    public double getRightVelocity()
    {
        return toFeet(right1.getSelectedSensorVelocity() * 10);
    }

}
