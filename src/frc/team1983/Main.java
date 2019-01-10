package frc.team1983;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main
{
    private Main()
    {

    }

    public static void main(String... args)
    {
        RobotBase.startRobot(Robot::new);
    }
}
