package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class CANSparkMax extends com.revrobotics.CANSparkMax implements Motor
{
    public CANSparkMax(int port, MotorType type, boolean reversed)
    {
        super(port, type);
        setInverted(reversed);
    }

    public CANSparkMax(int port, boolean reversed)
    {
        this(port, MotorType.kBrushless, reversed);
    }

    @Override
    public void set(ControlMode mode, double output)
    {
        if(mode == ControlMode.PercentOutput)
            set(output);
    }

    @Override
    public double getPositionTicks()
    {
        return 0;
    }

    @Override
    public double getVelocityTicks()
    {
        return 0;
    }

    @Override
    public void setBrake(boolean brake)
    {
        setIdleMode(brake ? IdleMode.kBrake : IdleMode.kCoast);
    }
}
