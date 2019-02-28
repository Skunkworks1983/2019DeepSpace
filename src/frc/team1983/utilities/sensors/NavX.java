package frc.team1983.utilities.sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

public class NavX extends AHRS implements Gyro
{
    public static final int NAVX_HEADING_SIGN = -1;

    private double offsetHeading = 0;
    private double offsetPitch = 0;
    private double offsetRoll = 0;

    public NavX()
    {
        super(SPI.Port.kMXP);
    }

    public double getHeading()
    {
        return getAngle() * NAVX_HEADING_SIGN + offsetHeading;
    }

    @Override
    public float getPitch()
    {
        return super.getRoll() + (float) offsetPitch;
    }

    @Override
    public float getRoll()
    {
        return super.getPitch() + (float) offsetRoll;
    }

    public void setHeading(double heading)
    {
        offsetHeading = heading - (getAngle() * NAVX_HEADING_SIGN);
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