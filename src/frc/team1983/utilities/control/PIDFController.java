package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.IMotionProfile;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.FeedbackType;
import frc.team1983.utilities.motors.Transmission;

import java.util.HashMap;
import java.util.function.Function;

public class PIDFController extends Thread
{
    private Transmission transmission;
    private double kP, kI, kD;
    private FeedbackType feedbackType;
    private HashMap<String, Function<Double, Double>> feedForwards;

    private double prevValue, prevTime, cumulativeError, target;
    private Logger logger = Logger.getInstance();
    private IMotionProfile motionProfile;
    private boolean runMotionProfile, enabled;
    private long profileStartTime;

    public PIDFController(Transmission transmission, double kP, double kI, double kD, FeedbackType feedbackType,
                          HashMap<String, Function<Double, Double>> feedForwards)
    {
        this.transmission = transmission;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.feedbackType = feedbackType;
        this.feedForwards = feedForwards;

        //These should probably be reset again before first execution, because construction usually happens a long time
        // before execution, but this at least prevents null pointer exceptions
        prevValue = transmission.getTicksPerSecond();
        prevTime = System.currentTimeMillis();
        cumulativeError = 0;
        target = 0;
        runMotionProfile = false;
        enabled = false;
    }

    public PIDFController(Transmission transmission, double kP, double kI, double kD, FeedbackType feedbackType)
    {
        this(transmission, kP, kI, kD, feedbackType, new HashMap<>());
    }

    @Override
    public void run()
    {
        if (!enabled) return;
        if (runMotionProfile)
        {
            // Cast 1000 to double to prevent integer division
            double time = (System.currentTimeMillis() - profileStartTime) / ((double) 1000);
            if (time > motionProfile.getLength())
            {
                target = 0;
                transmission.set(ControlMode.Throttle, 0);
                runMotionProfile = false;
                logger.info("PIDFController for " + transmission.getName() + " finished its motion profile",
                        this.getClass());
                return;
            }
            target = feedbackType == FeedbackType.POSITION ? motionProfile.calcPos(time) : motionProfile.calcVel(time);
        }
        transmission.set(ControlMode.Throttle, calculate(target));
    }

    /**
     * @param key      A string that can be used to access this feed forward term
     * @param function A lambda which takes in the current position of the system and returns a percentoutput
     *                 that will be added to the output
     */
    public synchronized void addFeedForwardFunction(String key, Function<Double, Double> function)
    {
        feedForwards.put(key, function);
    }

    /**
     * @param key The key of the feed forward function that should no longer be applied to the controller
     */
    public synchronized void removeFeedForwardFunction(String key)
    {
        feedForwards.remove(key);
    }

    /**
     * @param target The target value
     */
    private double calculate(double target)
    {
        double currentPos = transmission.getPositionTicks();
        double currentValue = feedbackType == FeedbackType.POSITION ? currentPos : transmission.getTicksPerSecond();
        double currentTime = System.currentTimeMillis();

        double error = target - currentValue; // Current error
        double de = currentValue - prevValue; // Change in error since last calculation
        double dt = currentTime - prevTime; // Change in time since last calculation
        double output; // Percent output to be applied to the motor

        output = error * kP;
        cumulativeError += error * dt;
        output += cumulativeError * kI;
        output -= de / dt * kD;

        for (Function<Double, Double> feedForward : feedForwards.values())
            output += feedForward.apply(currentPos);

        prevValue = currentValue;
        prevTime = currentTime;

        return output;
    }

    /**
     * Sets the target value of the system
     *
     * @param value The target value of the system in ticks / second
     */
    public synchronized void setTarget(double value)
    {
        runMotionProfile = false;
        target = value;
    }

    /**
     * Control the target with a motion profile
     */
    public synchronized void setMotionProfile(IMotionProfile motionProfile)
    {
        this.motionProfile = motionProfile;
        runMotionProfile = true;
        profileStartTime = System.currentTimeMillis();
    }

    public synchronized void setFeedbackType(FeedbackType feedbackType)
    {
        this.feedbackType = feedbackType;
        this.prevValue = feedbackType == FeedbackType.POSITION ? transmission.getPositionTicks() : transmission.getTicksPerSecond();
    }

    public synchronized FeedbackType getFeedbackType()
    {
        return feedbackType;
    }

    public synchronized void enable()
    {
        enabled = true;
    }

    public synchronized void disable()
    {
        transmission.set(ControlMode.Throttle, 0);
        enabled = false;
        runMotionProfile = false;
    }

    public synchronized boolean isEnabled()
    {
        return enabled;
    }

    public synchronized void setkP(double kP)
    {
        this.kP = kP;
    }

    public synchronized void setkI(double kI)
    {
        this.kI = kI;
    }

    public synchronized void setkD(double kD)
    {
        this.kD = kD;
    }
}
