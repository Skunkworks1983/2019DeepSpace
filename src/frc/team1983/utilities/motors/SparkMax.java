package frc.team1983.utilities.motors;

public class SparkMax extends com.revrobotics.CANSparkMax implements Motor
{
    public SparkMax(int port, MotorType type, boolean reversed)
    {
        super(port, type);
        setInverted(reversed);
    }

    public SparkMax(int port, boolean reversed)
    {
        this(port, MotorType.kBrushless, reversed);
    }

    @Override
    public void set(ControlMode mode, double output)
    {
        if(mode == ControlMode.PERCENT_OUTPUT) super.set(output);
    }

    @Override
    public void setBrake(boolean brake)
    {
        setIdleMode(brake ? IdleMode.kBrake : IdleMode.kCoast);
    }
}
