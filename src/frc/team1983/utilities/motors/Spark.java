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
//        // This means the position output will be multiplied by the spark internal encoder resolution number,
//        // effectively causing it to return ticks
//        encoder.setPositionConversionFactor(SPARK_INTERNAL_ENCODER_RESOLUTION);
//        // Same as above, but also divided by 60 so it's ticks per second, not ticks per minute
//        encoder.setVelocityConversionFactor(SPARK_INTERNAL_ENCODER_RESOLUTION / 60.0);
    }

    public Spark(int port, boolean reversed)
    {
        this(port, MotorType.kBrushless, reversed);
    }

    public void set(double output)
    {
        super.set(output);
    }

    @Override
    public void setBrake(boolean brake)
    {
        setIdleMode(brake ? IdleMode.kBrake : IdleMode.kCoast);
    }

    @Override
    public void setCurrentLimit(int limit)
    {
        this.setSmartCurrentLimit(limit);
    }

    @Override
    public void configure()
    {

    }

    @Override
    public double getPosition()
    {
        return encoder.getPosition();
    }

    @Override
    public double getVelocity()
    {
        return encoder.getVelocity();
    }
}
