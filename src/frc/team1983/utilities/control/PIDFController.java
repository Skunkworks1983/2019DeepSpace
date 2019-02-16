package frc.team1983.utilities.control;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.Transmission;

import java.util.ArrayList;
import java.util.function.Function;

public class PIDFController extends Thread
{
    public static final int UPDATE_RATE = 20;

    private PIDInput source;
    private PIDOutput output;
    private boolean useMotionProfiles = true;

    private MotionProfile motionProfile;

    private double kP, kI, kD;
    private double prevValue, prevTime, cumulativeError = 0, setpoint, profileStartTime;

    private ArrayList<Function<Double, Double>> feedforwards;

    private boolean enabled = false;

    public PIDFController(PIDInput source, PIDOutput output, double p, double i, double d, ArrayList<Function<Double, Double>> feedForwards)
    {
        this.source = source;
        this.output = output;
        setPID(p, i, d);

        this.feedforwards = feedForwards;
    }

    public PIDFController(Transmission transmission)
    {
        this(transmission, transmission, 0, 0, 0, new ArrayList<>());
    }

    public synchronized void setPID(double p, double i, double d)
    {
        this.kP = p;
        this.kI = i;
        this.kD = d;
    }

    public synchronized void addFeedforward(Function<Double, Double> feedForward)
    {
        feedforwards.add(feedForward);
    }

    protected void execute()
    {
        if (useMotionProfiles && motionProfile != null)
        {
            double time = Math.max((System.currentTimeMillis() / 1000.0) - profileStartTime, 0);

            if (time > motionProfile.getDuration())
                motionProfile = null;
            else
                setpoint = motionProfile.evaluate(Math.min(time, motionProfile.getDuration()));
        }
        double out = calculate(setpoint);
        //System.out.println(out);
        output.pidWrite(out);
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (!enabled) continue;

            if (kP == 0 && kI == 0 && kD == 0)
            {
                Logger.getInstance().warn("PID constants not configured", this.getClass());
                continue;
            }

            execute();

            try
            {
                Thread.sleep((long) 1000.0 / UPDATE_RATE);
            } catch (InterruptedException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    /**
     * @param setpoint The setpoint value
     */
    protected double calculate(double setpoint)
    {
        double currentValue = source.pidGet();
        double currentTime = System.currentTimeMillis() / 1000.0;

        double error = setpoint - currentValue; // Current error
        //todo update prevValue and prevTime
        double de = currentValue - prevValue; // Change in error since last calculation
        double dt = currentTime - prevTime; // Change in time since last calculation
        double output; // Percent output to be applied to the motor

        output = error * kP;
        cumulativeError += error * dt;
        output += cumulativeError * kI;
        output -= de / dt * kD;

        double currentTicks = source.getFeedForwardValue();
        for (Function<Double, Double> feedforward : feedforwards)
        {
            output += feedforward.apply(currentTicks);
        }

        return output;
    }

    public synchronized void setSetpoint(double value)
    {
        setpoint = value;
        useMotionProfiles = false;
        enable();
    }

    public synchronized void runMotionProfile(MotionProfile motionProfile)
    {
        this.motionProfile = motionProfile;
        useMotionProfiles = true;
        profileStartTime = System.currentTimeMillis() / 1000.0;
        enable();
    }

    public synchronized void enable()
    {
        prevValue = source.pidGet();
        prevTime = System.currentTimeMillis() / 1000.0;
        enabled = true;
    }

    public synchronized void disable()
    {
        enabled = false;
    }
}
