package frc.team1983.utilities.sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

public class NavX extends AHRS implements Gyro
{
    public static final int NAVX_HEADING_SIGN = -1;
    public static final double NAVX_HEADING_OFFSET = 90.0;

    private double offset = 0;

    public NavX()
    {
        super(SPI.Port.kMXP);
    }

    public double getHeading()
    {
        return getAngle() * NAVX_HEADING_SIGN + offset + NAVX_HEADING_OFFSET;
    }

    public void reset()
    {
        offset = 90.0 - getHeading();
    }
}