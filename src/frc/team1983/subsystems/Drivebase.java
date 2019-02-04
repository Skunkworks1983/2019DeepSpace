package frc.team1983.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.commands.drivebase.RunTankDrive;
import frc.team1983.constants.MotorMap;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Talon;
import frc.team1983.utilities.motors.Transmission;

public class Drivebase extends Subsystem
{
    public static final double INCHES_PER_TICK = (6.0 * Math.PI / 12.0) / 1360.0; // feet / tick of wheel encoders
    public static final double MAX_VELOCITY = 14.0; // feet / second, empirically measured maximum drive velocity in a straight line
    public static final double TRACK_WIDTH = 26.0 / 12.0; // feet, horizontal distance between left and right wheels

    private Transmission left, right;

    public Drivebase()
    {
        left = new Transmission(
                new Talon(MotorMap.Drivebase.LEFT_1, MotorMap.Drivebase.LEFT_1_REVERSED),
                new Talon(MotorMap.Drivebase.LEFT_2, MotorMap.Drivebase.LEFT_2_REVERSED),
                new Talon(MotorMap.Drivebase.LEFT_3, MotorMap.Drivebase.LEFT_3_REVERSED)
        );

        right = new Transmission(
                new Talon(MotorMap.Drivebase.RIGHT_1, MotorMap.Drivebase.RIGHT_1_REVERSED),
                new Talon(MotorMap.Drivebase.RIGHT_2, MotorMap.Drivebase.RIGHT_2_REVERSED),
                new Talon(MotorMap.Drivebase.RIGHT_3, MotorMap.Drivebase.RIGHT_3_REVERSED)
        );

        // todo: configure reduction of transmissions
        // todo: configure encoder properties of transmissions

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

    public double getRightPosition()
    {
        return right.getPositionInches();
    }


    public void setBrake(boolean brake)
    {
        left.setBrake(brake);
        right.setBrake(brake);
    }
}
