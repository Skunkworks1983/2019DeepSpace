package frc.team1983.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1983.constants.CollectorConstants;

public class Collector extends Subsystem
{
    private CANSparkMax wristRight, wristLeft;
    private CANSparkMax intakeRight, intakeLeft;
    private double targetPosition;
    private double initialEncoderPosition;

    public Collector()
    {
        wristRight = new CANSparkMax(CollectorConstants.WRIST_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        wristLeft = new CANSparkMax(CollectorConstants.WRIST_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);

        intakeRight = new CANSparkMax(CollectorConstants.INTAKE_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        intakeLeft = new CANSparkMax(CollectorConstants.INTAKE_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);

        wristRight.getPIDController().setP(CollectorConstants.WristGains.P);
        wristRight.getPIDController().setI(CollectorConstants.WristGains.I);
        wristRight.getPIDController().setD(CollectorConstants.WristGains.D);
        wristRight.getPIDController().setFF(0);

        initialEncoderPosition = wristRight.getEncoder().getPosition();
        //we know starting position now
    }
    @Override
    public void initDefaultCommand(){}
    @Override
    public void periodic(){}

    //MOVEMENT
    public void setIntakeLeft(double speed)
    {
        intakeLeft.set(speed);
    }
    public void setIntakeRight(double speed)
    {
        intakeRight.set(speed);
    }
    public void setWrist(double value) //todo: check
    {
        wristRight.set(value);
        wristLeft.follow(wristRight, CollectorConstants.WRIST_LEFT_REVERSED);
    }

    //TOOLS
    public static double toDegrees(double ticks){return ticks*CollectorConstants.WRIST_DEGREES_PER_TICK;}
    public static double toTicks(double degrees){return degrees/CollectorConstants.WRIST_DEGREES_PER_TICK;}
    public double getAngle(){return toDegrees(wristRight.getEncoder().getPosition());}
    public CANSparkMax getWristRight(){
        return wristRight;
    }
    public boolean atSetpoint()
    {
        return Math.abs(getAngle()- targetPosition) < CollectorConstants.WRIST_ALLOWABLE_ERROR;
    }
}
