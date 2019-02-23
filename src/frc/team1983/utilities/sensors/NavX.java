package frc.team1983.utilities.sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

public class NavX extends AHRS implements Gyro
{
    public static final int NAVX_HEADING_SIGN = -1;

    private double offset = 0;

    public NavX()
    {
        super(SPI.Port.kMXP);
    }

    public double getHeading()
    {
        return getAngle() * NAVX_HEADING_SIGN + offset;
    }

    public void setHeading(double heading)
    {
        offset = heading - (getAngle() * NAVX_HEADING_SIGN);
    }

    public void reset()
    {
        setHeading(0);
    }
}