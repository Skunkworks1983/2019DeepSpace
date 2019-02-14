package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Transmission;

public class PIDFController extends Thread
{
    private static final int UPDATE_RATE = 20;

    private Transmission transmission;
    private FeedbackType feedbackType;
    private boolean useMotionProfiles = true;

    private MotionProfile motionProfile;

    private double kP, kI, kD;
    private double prevValue, prevTime, cumulativeError = 0, setpoint;

    private boolean enabled = false;
    private long profileStartTime;

    public PIDFController(Transmission transmission, double p, double i, double d, FeedbackType feedbackType)
    {
        this.transmission = transmission;
        setPID(p, i, d);
        this.feedbackType = feedbackType;

        //These should probably be reset again before first execution, because construction usually happens a long time
        // before execution, but this at least prevents null pointer exceptions
        prevValue = transmission.getVelocityTicks();
        prevTime = System.currentTimeMillis();

        setpoint = transmission.getPositionInches();
    }

    public PIDFController(Transmission transmission, FeedbackType feedbackType)
    {
        this(transmission, 0, 0, 0, feedbackType);
    }

    public synchronized void setPID(double p, double i, double d)
    {
        this.kP = p;
        this.kI = i;
        this.kD = d;
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

            double adjustedSetpoint = setpoint;
            if(useMotionProfiles && motionProfile != null)
            {
                double time = (System.currentTimeMillis() - profileStartTime) / 1000.0;
                if(time > motionProfile.getDuration())
                    motionProfile = null;
                else
                    adjustedSetpoint = MotionProfile.evaluate(motionProfile, Math.min(time, motionProfile.getDuration()), feedbackType);
            }
            transmission.setRawThrottle(adjustedSetpoint);

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
        double currentValue = feedbackType == FeedbackType.POSITION ? transmission.getPositionInches() : transmission.getVelocityInches();
        double currentTime = System.currentTimeMillis();

        double error = setpoint - currentValue; // Current error
        double de = currentValue - prevValue; // Change in error since last calculation
        double dt = currentTime - prevTime; // Change in time since last calculation
        double output; // Percent output to be applied to the motor

        output = error * kP;
        cumulativeError += error * dt;
        output += cumulativeError * kI;
        output -= de / dt * kD;

        return output;
    }

    public synchronized void setSetpoint(double value)
    {
        setpoint = value;

        if(!useMotionProfiles) return;

        profileStartTime = System.currentTimeMillis();
        motionProfile = MotionProfile.generateTrapezoidalProfile(transmission.getPositionInches(), value, transmission.getMovementVelocity(), transmission.getMovementAcceleration());
    }

    public synchronized void setFeedbackType(FeedbackType feedbackType)
    {
        this.feedbackType = feedbackType;
        disable();
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
