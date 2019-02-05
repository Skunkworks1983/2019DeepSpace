package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Transmission;

import java.util.HashMap;
import java.util.function.Function;

public class VelocityPIDFController extends Thread
{
    private Transmission transmission;
    private double kP, kI, kD;
    private HashMap<String, Function<Double, Double>> feedForwards;

    private double prevVel, prevTime, cumulativeError, targetVelocity;
    private Logger logger = Logger.getInstance();
    private MotionProfile motionProfile;
    private boolean runMotionProfile, enabled;
    private long profileStartTime;

    public VelocityPIDFController(Transmission transmission, double kP, double kI, double kD,
                                  HashMap<String, Function<Double, Double>> feedForwards)
    {
        this.transmission = transmission;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.feedForwards = feedForwards;

        //These should probably be reset again before first execution, because construction usually happens a long time
        // before execution, but this at least prevents null pointer exceptions
        prevVel = transmission.getTicksPerSecond();
        prevTime = System.currentTimeMillis();
        cumulativeError = 0;
        targetVelocity = 0;
        runMotionProfile = false;
        enabled = false;
    }

    public VelocityPIDFController(Transmission transmission, double kP, double kI, double kD)
    {
        this(transmission, kP, kI, kD, new HashMap<>());
    }

    @Override
    public void run()
    {
        if (!enabled) return;
        if (runMotionProfile)
        {
            // Cast 1000 to double to prevent integer division
            double time = (System.currentTimeMillis() - profileStartTime) / ((double) 1000);
            if(time > motionProfile.getLength())
            {
                targetVelocity = 0;
                transmission.set(ControlMode.PercentOutput, 0);
                runMotionProfile = false;
                logger.info("VelocityPIDFController for " + transmission.getName() + " finished its motion profile",
                        this.getClass());
                return;
            }
            targetVelocity = motionProfile.calculate(time);
        }
        transmission.set(ControlMode.PercentOutput, calculateVelocityPIDF(targetVelocity));
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
     * @param key The key of the feed forward function that should no longer be applied to the velocity controller
     */
    public synchronized void removeFeedForwardFunction(String key)
    {
        feedForwards.remove(key);
    }

    /**
     * @param target The target velocity
     */
    private double calculateVelocityPIDF(double target)
    {
        double currentPos = transmission.getPositionTicks();
        double currentVel = transmission.getTicksPerSecond();
        double currentTime = System.currentTimeMillis();

        double error = target - currentVel; // Current error
        double de = currentVel - prevVel; // Change in error since last calculation
        double dt = currentTime - prevTime; // Change in time since last calculation
        double output; // Percent output to be applied to the motor

        output = error * kP;
        cumulativeError += error * dt;
        output += cumulativeError * kI;
        output -= de / dt * kD;

        for (Function<Double, Double> feedForward : feedForwards.values())
            output += feedForward.apply(currentPos);

        prevVel = currentVel;
        prevTime = currentTime;

        return output;
    }

    /**
     * Sets the target velocity of the system
     * @param value The target velocity of the system in ticks / second
     */
    public synchronized void setTargetVelocity(double value)
    {
        runMotionProfile = false;
        targetVelocity = value;
    }

    /**
     * Control the velocity with a motion profile
     */
    public synchronized void setMotionProfile(MotionProfile motionProfile)
    {
        this.motionProfile = motionProfile;
        runMotionProfile = true;
        profileStartTime = System.currentTimeMillis();
    }

    public synchronized void enable()
    {
        enabled = true;
    }

    public synchronized void disable()
    {
        transmission.set(ControlMode.PercentOutput, 0);
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
