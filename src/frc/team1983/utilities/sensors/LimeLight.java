package frc.team1983.utilities.sensors;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
public class LimeLight implements Runnable
{
    public static final int UPDATE_RATE = 20;

    protected static final boolean TARGET_DETECTED_DEFAULT_VALUE = false;
    protected static final double X_DEFAULT_VALUE = 0.0;
    protected static final double Y_DEFAULT_VALUE = 0.0;
    protected static final double AREA_DEFAULT_VALUE = 0.0;
    protected static final double SKEW_DEFAULT_VALUE = 0.0;
    protected static final double X_OFFSET_DEFAULT_VALUE = 0.0;
    protected static final double Y_OFFSET_DEFAULT_VALUE = 0.0;
    protected static final double PITCH_DEFAULT_VALUE = 0.0;
    protected static final double YAW_DEFAULT_VALUE = 0.0;
    protected static final double ROLL_DEFAULT_VALUE = 0.0;

    private boolean targetDetected;
    private double x, y;
    private double area;
    private double skew;
    private double xOffset, yOffset;
    private double pitch, yaw, roll;

    public LimeLight()
    {
        new Thread(this).start();
    }

    protected synchronized void execute()
    {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        // Update values
        targetDetected = table.getEntry("tv").getDouble(TARGET_DETECTED_DEFAULT_VALUE ? 1 : 0) == 1;
        x = table.getEntry("tx").getDouble(X_DEFAULT_VALUE);
        y = table.getEntry("ty").getDouble(Y_DEFAULT_VALUE);
        area = table.getEntry("ta").getDouble(AREA_DEFAULT_VALUE);
        skew = table.getEntry("ts").getDouble(SKEW_DEFAULT_VALUE);
        double[] pnp = table.getEntry("camtran").getDoubleArray(new double[]{X_OFFSET_DEFAULT_VALUE, 0.0, Y_OFFSET_DEFAULT_VALUE, PITCH_DEFAULT_VALUE, YAW_DEFAULT_VALUE, ROLL_DEFAULT_VALUE});

        xOffset = pnp[0];
        yOffset = pnp[2];
        pitch = pnp[3];
        yaw = pnp[4];
        roll = pnp[5];
    }

    public boolean isTargetDetected()
    {
        return targetDetected;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getArea()
    {
        return area;
    }

    public double getSkew()
    {
        return skew;
    }

    public double getXOffset()
    {
        return xOffset;
    }

    public double getYOffset()
    {
        return yOffset;
    }

    public double getPitch()
    {
        return pitch;
    }

    public double getYaw()
    {
        return yaw;
    }

    public double getRoll()
    {
        return roll;
    }

    @Override
    public void run()
    {
        while(true)
        {
            execute();

            try
            {
                Thread.sleep((long) 1000.0 / UPDATE_RATE);
            }
            catch(InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }
    }
}
