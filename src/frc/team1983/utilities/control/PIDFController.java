package frc.team1983.utilities.control;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Transmission;

public class PIDFController extends Thread
{
    private static final int UPDATE_RATE = 20;

    private Transmission transmission;
    private FeedbackType feedbackType;

    private MotionProfile motionProfile;

    private double kP, kI, kD;
    private double prevValue, prevTime, cumulativeError, target;

    private boolean enabled = false;
    private long profileStartTime;

    public PIDFController(Transmission transmission, double kP, double kI, double kD, FeedbackType feedbackType)
    {
        this.transmission = transmission;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.feedbackType = feedbackType;

        //These should probably be reset again before first execution, because construction usually happens a long time
        // before execution, but this at least prevents null pointer exceptions
        prevValue = transmission.getVelocityTicks();
        prevTime = System.currentTimeMillis();

        target = transmission.getPositionInches();
        cumulativeError = 0;
    }

    public PIDFController(Transmission transmission, FeedbackType feedbackType)
    {
        this(transmission, 0, 0, 0, feedbackType);
    }

    public PIDFController(Transmission transmission)
    {
        this(transmission, FeedbackType.POSITION);
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

            if(motionProfile != null)
            {
                // Cast 1000 to double to prevent integer division
                double time = (System.currentTimeMillis() - profileStartTime) / 1000.0;

                if(time > motionProfile.getDuration())
                    motionProfile = null;
                else
                    transmission.setRawThrottle(calculate(MotionProfile.evaluate(motionProfile, Math.min(time, motionProfile.getDuration()), feedbackType)));
            }
            else
            {
                transmission.setRawThrottle(calculate(target));
            }

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
     * @param target The target value
     */
    private double calculate(double target)
    {
        double currentValue = feedbackType == FeedbackType.POSITION ? transmission.getPositionInches() : transmission.getVelocityInches();
        double currentTime = System.currentTimeMillis();

        double error = target - currentValue; // Current error
        double de = currentValue - prevValue; // Change in error since last calculation
        double dt = currentTime - prevTime; // Change in time since last calculation
        double output; // Percent output to be applied to the motor

        output = error * kP;
        cumulativeError += error * dt;
        output += cumulativeError * kI;
        output -= de / dt * kD;

        prevValue = currentValue;
        prevTime = currentTime;

        return output;
    }

    public synchronized void setTarget(double value)
    {
        target = value;
        profileStartTime = System.currentTimeMillis();
        motionProfile = MotionProfile.generateTrapezoidalProfile(transmission.getPositionInches(), value, transmission.getMovementVelocity(), transmission.getMovementAcceleration());
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
