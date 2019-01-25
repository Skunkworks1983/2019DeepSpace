package frc.team1983;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team1983.constants.MotorMap;
import frc.team1983.services.OI;
import frc.team1983.services.StateEstimator;
import frc.team1983.services.logging.Level;
import frc.team1983.services.logging.Logger;
import frc.team1983.subsystems.Drivebase;
import frc.team1983.commands.collector.StubbornThread;

public class Robot extends TimedRobot
{
    private static Robot instance;
    private Drivebase drivebase;
    private StateEstimator estimator;
    private PigeonIMU pigeon;
    private OI oi;
    private Logger logger;
    private double stubbornInitialPos;
    private StubbornThread stubbornThread;

    Robot()
    {
        instance = this;
    }

    @Override
    public void robotInit()
    {
        logger = Logger.getInstance();
        logger.setGlobalLevel(Level.INFO);
        logger.info("Logging test", getClass());

        drivebase = new Drivebase();
        //estimator = new StateEstimator();
        pigeon = new PigeonIMU(MotorMap.Drivebase.LEFT_1);
        oi = new OI();
        stubbornThread = new StubbornThread();

        //new Thread(estimator).start();
    }
    @Override
    public void teleopInit()
    {
        //stubbornInitialPos = new AnalogInput(0).getValue();
        //System.out.println("initial position: "+stubbornInitialPos);
        Scheduler.getInstance().add(stubbornThread);
    }
    @Override
    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();
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

    public StateEstimator getEstimator()
    {
        return estimator;
    }

    public PigeonIMU getPigeon()
    {
        return pigeon;
    }

    public OI getOI()
    {
        return oi;
    }
}
