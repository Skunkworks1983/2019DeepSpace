package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Transmission;

import java.util.ArrayList;
import java.util.function.Function;

public class PIDFController extends Thread
{
    private static final int UPDATE_RATE = 20;

    private PIDSource source;
    private PIDOutput output;
    private boolean useMotionProfiles = true;

    private MotionProfile motionProfile;

    private double kP, kI, kD;
    private double prevValue, prevTime, cumulativeError = 0, setpoint, profileStartTime;

    private ArrayList<Function<Double, Double>> feedforwards;

    private boolean enabled = false;

    public PIDFController(PIDSource source, PIDOutput output, double p, double i, double d, ArrayList<Function<Double, Double>> feedForwards)
    {
        this.source = source;
        this.output = output;
        setPID(p, i, d);

        this.feedforwards = feedForwards;

        //These should probably be reset again before first execution, because construction usually happens a long time
        // before execution, but this at least prevents null pointer exceptions
        prevValue = source.pidGet();
        prevTime = System.currentTimeMillis() / 1000.0;

        setpoint = source.getPositionTicks();
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

    protected synchronized void execute()
    {
        if(useMotionProfiles && motionProfile != null)
        {
            double time = (System.currentTimeMillis() / 1000.0) - profileStartTime;
            if(time > motionProfile.getDuration())
                motionProfile = null;
            else
                setpoint = motionProfile.evaluate(Math.min(time, motionProfile.getDuration()));
        }
        output.pidWrite(calculate(setpoint));
    }

    @Override
    public void run()
    {
        while(true)
        {
            if(!enabled) continue;

            if(kP == 0 && kI == 0 && kD == 0)
            {
                Logger.getInstance().warn("PID constants not configured", this.getClass());
                continue;
            }

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

    /**
     * @param setpoint The setpoint value
     */
    private double calculate(double setpoint)
    {
        double currentValue = source.pidGet();
        double currentTime = System.currentTimeMillis() / 1000.0;

        double error = setpoint - currentValue; // Current error
        double de = currentValue - prevValue; // Change in error since last calculation
        double dt = currentTime - prevTime; // Change in time since last calculation
        double output; // Percent output to be applied to the motor

        output = error * kP;
        cumulativeError += error * dt;
        output += cumulativeError * kI;
        output -= de / dt * kD;

        double currentTicks = source.getPositionTicks();
        for(Function<Double, Double> feedforward : feedforwards)
        {
            output += feedforward.apply(currentTicks);
        }

        return output;
    }

    public synchronized void setSetpoint(double value)
    {
        setpoint = value;
        useMotionProfiles = false;
    }

    // Don't forget to update your pidsource's feedback type!
    public synchronized void runMotionProfile(MotionProfile motionProfile)
    {
        this.motionProfile = motionProfile;
        useMotionProfiles = true;
        profileStartTime = System.currentTimeMillis() / 1000.0;
    }

    public synchronized void enable()
    {
        enabled = true;
    }

    public synchronized void disable()
    {
        enabled = false;
    }
}
