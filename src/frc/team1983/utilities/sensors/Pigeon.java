package frc.team1983.utilities.sensors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class Pigeon extends PigeonIMU
{
    public Pigeon(TalonSRX talon)
    {
        super(talon);
    }

    public double getHeading()
    {
        return getFusedHeading() + 90.0;
    }
}
