package frc.team1983.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.Constants;
import frc.team1983.commands.drivebase.RunTankDrive;

public class Drivebase extends Subsystem
{
    private TalonSRX left1, left2, left3;
    private TalonSRX right1, right2, right3;

    public Drivebase()
    {
        left1 = new TalonSRX(Constants.MotorMap.Drivebase.LEFT_1);
        left1.setInverted(Constants.MotorMap.Drivebase.LEFT_1_REVERSED);
        left2 = new TalonSRX(Constants.MotorMap.Drivebase.LEFT_2);
        left2.setInverted(Constants.MotorMap.Drivebase.LEFT_2_REVERSED);
        left2.follow(left1);
        left3 = new TalonSRX(Constants.MotorMap.Drivebase.LEFT_3);
        left3.setInverted(Constants.MotorMap.Drivebase.LEFT_3_REVERSED);
        left3.follow(left2);

        right1 = new TalonSRX(Constants.MotorMap.Drivebase.RIGHT_1);
        right1.setInverted(Constants.MotorMap.Drivebase.RIGHT_1_REVERSED);
        right2 = new TalonSRX(Constants.MotorMap.Drivebase.RIGHT_2);
        right2.setInverted(Constants.MotorMap.Drivebase.RIGHT_2_REVERSED);
        right2.follow(right1);
        right3 = new TalonSRX(Constants.MotorMap.Drivebase.RIGHT_3);
        right3.setInverted(Constants.MotorMap.Drivebase.RIGHT_3_REVERSED);
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

    public void zero()
    {
        left1.setSelectedSensorPosition(0);
        right1.setSelectedSensorPosition(0);
    }

    public void setLeft(ControlMode mode, double value)
    {
        left1.set(mode, value);
    }

    public void setRight(ControlMode mode, double value)
    {
        right1.set(mode, value);
    }

    public static double toInches(double ticks)
    {
        return ticks * Constants.DRIVEBASE_INCHES_PER_TICK;
    }

    public static double toTicks(double inches)
    {
        return inches / Constants.DRIVEBASE_INCHES_PER_TICK;
    }

    public double getLeftPosition()
    {
        return toInches(left1.getSelectedSensorPosition());
    }

    public double getRightPosition()
    {
        return toInches(right1.getSelectedSensorPosition());
    }

    public double getLeftVelocity()
    {
        return toInches(left1.getSelectedSensorVelocity() * 10);
    }

    public double getRightVelocity()
    {
        return toInches(right1.getSelectedSensorVelocity() * 10);
    }

}
