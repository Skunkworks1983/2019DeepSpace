package frc.team1983.utilities.sensors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class Pigeon extends PigeonIMU implements Gyro
{
    public static final int PIGEON_HEADING_SIGN = 1;
    public static final double PIGEON_HEADING_OFFSET = 90.0;

    private double offset = 0;

    public Pigeon(TalonSRX talon)
    {
        super(talon);
    }

    public double getHeading()
    {
        return getFusedHeading() * PIGEON_HEADING_SIGN + offset + PIGEON_HEADING_OFFSET;
    }

    public void reset()
    {
        offset = 90.0 - getHeading();
    }
}
