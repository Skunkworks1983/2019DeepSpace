package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.RobotMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Talon;

public class Lift extends Subsystem
{
    public static final double TICKS_PER_INCH = 0; //TODO: add math

    public Talon liftMotor;

    public Lift()
    {
        liftMotor = new Talon(0, true);

        liftMotor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, 0);

        //TODO: Add PID Values
        liftMotor.config_kP(0,0);
        liftMotor.config_kI(0,0);
        liftMotor.config_kD(0,0);
    }


    //TODO: Define: setTicksPerInch(), setMovementAcceleration(), setMovementV(), and setPID().


    @Override
    protected void initDefaultCommand()
    {

    }

    public void periodic()
    {

    }

    public void zero()
    {
        liftMotor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, 0); }

    public void set(ControlMode mode, double value)
    {
        liftMotor.set(mode.TALON, value);
    }

    public double getPosition()
    {
        return liftMotor.getPosition();
    }

    public double getVelocity()
    {
        return liftMotor.getVelocity();
    }
}
