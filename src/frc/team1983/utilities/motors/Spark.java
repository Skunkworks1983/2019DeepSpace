package frc.team1983.utilities.motors;

import frc.team1983.utilities.sensors.Encoder;

public class Spark extends com.revrobotics.CANSparkMax implements Motor, Encoder
{
    public Spark(int port, MotorType type, boolean reversed)
    {
        super(port, type);
        setInverted(reversed);
    }

    public Spark(int port, boolean reversed)
    {
        this(port, MotorType.kBrushless, reversed);
    }

    @Override
    public void set(ControlMode mode, double output)
    {
        if(mode == ControlMode.Throttle) super.set(output);
    }

    @Override
    public void setBrake(boolean brake)
    {
        setIdleMode(brake ? IdleMode.kBrake : IdleMode.kCoast);
    }

    @Override
    public void configure()
    {

    }

    @Override
    public int getPosition()
    {
        return (int) getEncoder().getPosition();
    }

    @Override
    public double getVelocity()
    {
        return (int) getEncoder().getVelocity();
    }
}
