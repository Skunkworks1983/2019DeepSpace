package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.Transmission;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * This is our custom implementation of a PIDF Controller. It also can be passed motion profiles, and allows for arbitrary
 * feedforward functions. A PIDF Controller is a closed loop controller that drives a system towards a given setpoint
 * by adding together several things. There are many explanations online for what a PID controller is, and any senior
 * member of the team should be able to explain it better to you than I can in a javadoc.
 */
public class PIDFController extends Thread
{
    // After every execution this thread will sleep for a bit. A higher update rate reduces this wait time, effectively
    // making the thread update more often.
    public static final int UPDATE_RATE = 20;

    private PIDInput input;
    private PIDOutput output;

    private MotionProfile motionProfile;

    private double kP, kI, kD;
    public double prevValue, prevTime, cumulativeError = 0, setpoint, profileStartTime;

    private ArrayList<Function<Double, Double>> feedforwards;

    private boolean enabled = false;

    /**
     * @param source The input to the closed loop. Usually an encoder or transmission.
     * @param output The object that will be fed the output. Usually a motor or transmission.
     * @param p The proportional gain
     * @param i The integral gain
     * @param d The derivative gain
     * @param feedForwards An array of Function objects, which will be passed the output of source.getFeedForward() and
     *                     should return a value that will be added to the final output.
     */
    public PIDFController(PIDInput source, PIDOutput output, double p, double i, double d, ArrayList<Function<Double, Double>> feedForwards)
    {
        this.input = source;
        this.output = output;
        setPID(p, i, d);

        this.feedforwards = feedForwards;
    }

    /**
     * A constructor which passes a given transmission as source and output, zeros all the gains,
     * and initializes an empty array for feedforwards.
     * @param transmission The transmission that this PIDFController should control
     */
    public PIDFController(Transmission transmission)
    {
        this(transmission, transmission, 0, 0, 0, new ArrayList<>());
    }

    /**
     * Sets the PID gains
     */
    public synchronized void setPID(double p, double i, double d)
    {
        this.kP = p;
        this.kI = i;
        this.kD = d;
    }

    /**
     * Setter method for adding a new arbitrary feedforward function
     * @param feedForward The new feedforward function
     */
    public synchronized void addFeedforward(Function<Double, Double> feedForward)
    {
        feedforwards.add(feedForward);
    }

    /**
     * This method only exists for unit testing purposes. It evaluates the motion profile and calculates then writes
     * the PIDF output
     */
    protected void execute()
    {
        if (motionProfile != null)
        {
            double time = Math.max((System.currentTimeMillis() / 1000.0) - profileStartTime, 0);

            if (time > motionProfile.getDuration())
                motionProfile = null;
            else
                setpoint = motionProfile.evaluate(Math.min(time, motionProfile.getDuration()));
        }
        double out = calculate(setpoint);
        output.pidWrite(out);
    }

    /**
     * Called by the system when this thread is started.
     */
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
     * Calculates the PIDF output
     * @param setpoint The setpoint value
     * @return the calculated output
     */
    protected double calculate(double setpoint)
    {
        double currentValue = input.pidGet();
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

        double currentTicks = input.getFeedForwardValue();
        for (Function<Double, Double> feedforward : feedforwards)
            output += feedforward.apply(currentTicks);

        return output;
    }

    public synchronized void setSetpoint(double value)
    {
        setpoint = value;
        motionProfile = null;
        enable();
    }

    /**
     * Starts a motion profile and enables the controller
     * @param motionProfile The motion profile to be run
     */
    public synchronized void runMotionProfile(MotionProfile motionProfile)
    {
        this.motionProfile = motionProfile;
        profileStartTime = System.currentTimeMillis() / 1000.0;
        enable();
    }

    /**
     * Enables the controller and sets the prev variables to prevent timing issues
     */
    public synchronized void enable()
    {
        enabled = true;
    }

    /**
     * Disables the controller
     */
    public synchronized void disable()
    {
        enabled = false;
        motionProfile = null;
    }
}
