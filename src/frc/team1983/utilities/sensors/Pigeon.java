package frc.team1983.utilities.sensors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class Pigeon extends PigeonIMU implements Gyro
{
    public static final int PIGEON_HEADING_SIGN = 1;

    private double offset = 0;

    public Pigeon(TalonSRX talon)
    {
        super(talon);
    }

    public double getHeading()
    {
        return getFusedHeading() * PIGEON_HEADING_SIGN + offset;
    }

    public void setHeading(double heading)
    {
        offset = heading - (getFusedHeading() * PIGEON_HEADING_SIGN);
    }

    public void reset()
    {
        setHeading(0);
    }
}
