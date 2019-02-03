package frc.team1983.utilities.control;

public class CANSparkMax extends com.revrobotics.CANSparkMax implements Motor
{
    public CANSparkMax(int deviceID, MotorType type)
    {
        super(deviceID, type);
    }
}
