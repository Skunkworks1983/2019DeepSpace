package frc.team1983.utilities.motors;

import com.revrobotics.CANEncoder;
import frc.team1983.utilities.sensors.Encoder;

public class Spark extends com.revrobotics.CANSparkMax implements Motor, Encoder
{
    public static final int SPARK_INTERNAL_ENCODER_RESOLUTION = 42;
    private CANEncoder encoder;

    public Spark(int port, MotorType type, boolean reversed)
    {
        super(port, type);
        setInverted(reversed);
        encoder = getEncoder();
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
    public double getPosition()
    {
        return (getInverted() ? -1 : 1) * encoder.getPosition();
    }

    @Override
    public double getVelocity()
    {
        return (getInverted() ? -1 : 1) * encoder.getVelocity() / SPARK_INTERNAL_ENCODER_RESOLUTION;
    }
}
