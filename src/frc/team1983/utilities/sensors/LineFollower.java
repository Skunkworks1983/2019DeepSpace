package frc.team1983.utilities.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

public class LineFollower
{
    // TODO: find real
    public static final int RIGHT_PORT = 0;
    public static final int LEFT1_PORT = 1;
    public static final int LEFT2_PORT = 2;

    private AnalogInput right, left1, left2;

    public LineFollower()
    {
        right = new AnalogInput(RIGHT_PORT);
        left1 = new AnalogInput(LEFT1_PORT);
        left2 = new AnalogInput(LEFT2_PORT);
    }
}
