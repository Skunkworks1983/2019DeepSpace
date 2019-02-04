package frc.team1983.utilities.control;

import frc.team1983.utilities.motors.ControlMode;
import frc.team1983.utilities.motors.Transmission;

import java.util.HashMap;
import java.util.function.Function;

public class Profiler
{
    private Transmission transmission;
    private double kP, kI, kD;
    private HashMap<String, Function<Double, Double>> feedForwards;
    private double prevVel, prevTime, cumulativeError;

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
    }

    public Profiler(Transmission transmission, double kP, double kI, double kD)
    {
        this(transmission, kP, kI, kD, new HashMap<>());
    }

    /**
     * @param key A string that can be used to access this feed forward term
     * @param function A lambda which takes in the current position of the system and returns a percentoutput
     *                 that will be added to the output
     */
    public void addFeedForwardFunction(String key, Function<Double, Double> function)
    {
        feedForwards.put(key, function);
    }

    /**
     * @param key The key of the function that should no longer be applied
     */
    public void removeFeedForwardFunction(String key)
    {
        feedForwards.remove(key);
    }

    /**
     * @param target The target velocity
     */
    public void calculate(double target)
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
        {
            output += feedForward.apply(currentPos);
        }

        transmission.set(ControlMode.PERCENT_OUTPUT, output);
    }

    public void setkP(double kP)
    {
        this.kP = kP;
    }

    public void setkI(double kI)
    {
        this.kI = kI;
    }

    public void setkD(double kD)
    {
        this.kD = kD;
    }
}
