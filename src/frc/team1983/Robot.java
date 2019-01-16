package frc.team1983;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team1983.commands.drivebase.DrivePath;
import frc.team1983.constants.MotorMap;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.Drivebase;

public class Robot extends TimedRobot
{
    private static Robot instance;
    private Drivebase drivebase;
    private PigeonIMU pigeon;
    private StateEstimator estimator;
    private OI oi;
    private Logger logger;

    Robot()
    {
        instance = this;

        logger = Logger.getInstance();
        logger.setGlobalLevel(Level.INFO);

        drivebase = new Drivebase();
        pigeon = new PigeonIMU(MotorMap.Drivebase.LEFT_1);
        estimator = new StateEstimator();
        oi = new OI();

        oi.initializeBindings();
    }

    @Override
    public void robotPeriodic()
    {
        Scheduler.getInstance().run();
    }

    public static Robot getInstance()
    {
        if(instance == null)
            instance = new Robot();
        return instance;
    }

    public Drivebase getDrivebase()
    {
        return drivebase;
    }

    public PigeonIMU getPigeon()
    {
        return pigeon;
    }

    public StateEstimator getEstimator()
    {
        return estimator;
    }

    public OI getOI()
    {
        return oi;
    }
}
