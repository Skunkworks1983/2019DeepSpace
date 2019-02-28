package frc.team1983.utilities.sensors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class Pigeon extends PigeonIMU implements Gyro
{
    public static final int PIGEON_HEADING_SIGN = 1;

    private double offsetHeading = 0;
    private double offsetPitch = 0;
    private double offsetRoll = 0;

    public Pigeon(TalonSRX talon)
    {
        super(talon);
    }

    @Override
    public float getPitch()
    {
        return 0; // todo implement
    }

    @Override
    public float getRoll()
    {
        return 0; // todo implement
    }

    public double getHeading()
    {
        return getFusedHeading() * PIGEON_HEADING_SIGN + offsetHeading;
    }

    public void setHeading(double heading)
    {
        offsetHeading = heading - (getFusedHeading() * PIGEON_HEADING_SIGN);
    }

    public void setPitch(float pitch)
    {
        offsetPitch = pitch - getPitch();
    }

    public void setRoll(float roll)
    {
        offsetRoll = roll - getRoll();
    }

    public void reset()
    {
        setHeading(0);
        setPitch(0);
        setRoll(0);

    }
}
