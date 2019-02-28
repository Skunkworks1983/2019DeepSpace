package frc.team1983.utilities.control;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.MotorGroup;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * This is our custom implementation of a PIDF Controller. It also can be passed motion profiles, and allows for arbitrary
 * feedforward functions. A PIDF Controller is a closed loop controller that drives a system towards a given setpoint
 * by adding together several things. There are many explanations online for what a PID controller is, and any senior
 * member of the team should be able to explain it better to you than I can in a javadoc.
 */
public class MotorGroupController extends Thread
{
    // After every execution this thread will sleep for a bit. A higher update rate reduces this wait time, effectively
    // making the thread update more often.
    public static final int UPDATE_RATE = 20;

    private PIDInput input, leader;
    private PIDOutput output;

    protected MotionProfile motionProfile;

    private double kP, setpoint, profileStartTime;

    private ArrayList<Function<Object, Double>> ffTerms;

    private boolean enabled = false;

    /**
     * @param input   The input to the closed loop. Usually an encoder or motorGroup.
     * @param output  The object that will be fed the output. Usually a motor or motorGroup.
     * @param kP      The proportional gain
     * @param ffTerms An array of Function objects, which will be passed the output of input.getFF() and
     *                should return a value that will be added to the final output.
     */
    public MotorGroupController(PIDInput input, PIDOutput output, double kP,
                                ArrayList<Function<Object, Double>> ffTerms)
    {
        this.input = input;
        this.output = output;
        this.kP = kP;

        this.ffTerms = ffTerms;
    }

    /**
     * A constructor which passes a given motorGroup as source and output, zeros all the gains,
     * and initializes an empty array for ffTerms.
     *
     * @param motorGroup The motorGroup that this MotorGroupController should control
     */
    public MotorGroupController(MotorGroup motorGroup)
    {
        this(motorGroup, motorGroup, 0, new ArrayList<>());
    }

    /**
     * Sets the PID gains
     */
    public synchronized void setKP(double kP)
    {
        this.kP = kP;
    }

    public synchronized double getKP()
    {
        return kP;
    }

    /**
     * Setter method for adding a new arbitrary feedforward function
     *
     * @param feedForward The new feedforward function
     */
    public synchronized void addFeedforward(Function<Object, Double> feedForward)
    {
        ffTerms.add(feedForward);
    }

    /**
     * This method only exists for unit testing purposes. It evaluates the motion profile and calculates then writes
     * the PIDF output
     */
    protected void execute()
    {
        try
        {
            Thread.sleep((long) 1000.0 / UPDATE_RATE);
        } catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }

        if (!enabled) return;

        if (kP == 0)
        {
            Logger.getInstance().warn("P gain not configured", this.getClass());
            return;
        }

        double target = setpoint;

        if(leader != null)
        {
            target = leader.pidGet();
        }
        else if (motionProfile != null)
        {
            double time = Math.max((System.currentTimeMillis() / 1000.0) - profileStartTime, 0);

            if (time > motionProfile.getDuration())
            {
                setpoint = motionProfile.getEndpoint();
                target = setpoint;
                motionProfile = null;
            }
            else target = motionProfile.evaluate(Math.min(time, motionProfile.getDuration()));
        }
        output.pidWrite(calculate(target));
    }

    /**
     * Called by the system when this thread is started.
     */
    @Override
    public void run()
    {
        while (true)
        {
            execute();
        }
    }

    /**
     * Calculates the PIDF output
     *
     * @param setpoint The setpoint value
     * @return the calculated output
     */
    protected double calculate(double setpoint)
    {
        double currentValue = input.pidGet();

        double error = setpoint - currentValue; // Current error

        double output; // Percent output to be applied to the motor

        output = error * kP;

        Object ffOperator = input.getFFOperator();
        for (Function<Object, Double> ffTerm : ffTerms)
            output += ffTerm.apply(ffOperator);

        return output;
    }

    public synchronized void setSetpoint(double value)
    {
        setpoint = value;
        motionProfile = null;
        leader = null;
        enable();
    }

    /**
     * Starts a motion profile and enables the controller
     *
     * @param motionProfile The motion profile to be run
     */
    public synchronized void runMotionProfile(MotionProfile motionProfile)
    {
        this.motionProfile = motionProfile;
        leader = null;
        profileStartTime = System.currentTimeMillis() / 1000.0;
        enable();
    }

    public synchronized void follow(PIDInput leader)
    {
        this.leader = leader;
        motionProfile = null;
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
        if (leader == null)
        {
            enabled = false;
            motionProfile = null;
        }
    }
}
