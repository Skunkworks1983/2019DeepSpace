package frc.team1983.utilities.control;

public class CANSparkMax extends com.revrobotics.CANSparkMax implements Motor
{
    public CANSparkMax(int port, MotorType type, boolean reversed)
    {
        super(port, type);
        setInverted(reversed);
    }

    @Override
    public void setBrake(boolean brake)
    {
        setIdleMode(brake ? IdleMode.kBrake : IdleMode.kCoast);
    }
}
