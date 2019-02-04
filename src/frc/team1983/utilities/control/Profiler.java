package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Transmission;

import java.util.HashMap;
import java.util.function.Function;

public class Profiler extends Thread
{
    private Transmission transmission;
    private double kP, kI, kD;
    private HashMap<String, Function<Double, Double>> feedForwards;

    private double prevVel, prevTime, cumulativeError, targetVelocity;
    private ControlMode controlMode;
    private Logger logger = Logger.getInstance();

    public Profiler(Transmission transmission, double kP, double kI, double kD,
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
        controlMode = ControlMode.PercentOutput;
    }

    public Profiler(Transmission transmission, double kP, double kI, double kD)
    {
        this(transmission, kP, kI, kD, new HashMap<>());
    }

    @Override
    public void run()
    {
        if(controlMode != ControlMode.MotionMagic && controlMode != ControlMode.Velocity) return;
        if(controlMode == ControlMode.MotionMagic) targetVelocity = calculateMotionmagic();
        calculateVelocityPIDF(targetVelocity);
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
    private void calculateVelocityPIDF(double target)
    {
        double currentVel = transmission.getTicksPerSecond();
        double currentTime = System.currentTimeMillis();
        double currentPos = transmission.getPositionTicks();

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

        transmission.set(ControlMode.PercentOutput, output);

        prevVel = currentVel;
        prevTime = currentTime;
    }

    /**
     * General function for setting the transmission. Generally should only be MotionMagic or Velocity, but
     * PercentOutput is also supported.
     * @param controlMode The control mode that the value should set to
     * @param value For PercentOutput: double between -1 and 1. For Velocity: a speed (ticks / second), not bounded by
     *              the configured max velocity or acceleration, so be careful. For motion magic, the setpoint that
     *              the system should be driven towards, bound by the min and max system positions
     */
    public synchronized void set(ControlMode controlMode, double value)
    {
        this.controlMode = controlMode;
        switch (controlMode)
        {
            case PercentOutput:
                transmission.set(controlMode, value);
                break;
            case MotionMagic:
                initMotionMagic((int) value);
                break;
            case Velocity:
                targetVelocity = value;
                break;
            default:
                logger.warn("Profiler for " + transmission.getName() + " was set to an invalid control mode",
                        this.getClass());
        }
    }

    private void initMotionMagic(int setpoint)
    {

    }

    private double calculateMotionmagic()
    {
        return 0; //TODO
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
