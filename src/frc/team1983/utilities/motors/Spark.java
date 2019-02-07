package frc.team1983.utilities.motors;

public class Spark extends com.revrobotics.CANSparkMax implements Motor
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
}
