package frc.team1983;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot
{
    private static Robot instance;

    public Robot()
    {

    }

    public static Robot getInstance()
    {
        if(instance == null)
            instance = new Robot();
        return instance;
    }
}
