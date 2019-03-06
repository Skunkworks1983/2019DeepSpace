package frc.team1983.utilities.control;

import frc.team1983.Robot;
import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.motors.MotorGroup;

import java.util.NavigableMap;
import java.util.TreeMap;

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
    public static final int DEFAULT_INTEGRAL_MEMORY = 10; // seconds

    private PIDInput input;
    private PIDOutput output;

    protected MotionProfile motionProfile;

    private double kP, kI, kD, kF;
    private double integralMemory = DEFAULT_INTEGRAL_MEMORY;
    private double errorIntegral;
    private TreeMap<Double, Double> errors = new TreeMap<>(); // First value is time in seconds, second is error
    private double setpoint, profileStartTime;

    private boolean enabled = false;

    /**
     * @param input   The input to the closed loop. Usually an encoder or motorGroup.
     * @param output  The object that will be fed the output. Usually a motor or motorGroup.
     * @param kP      The proportional gain
     */
    public PIDFController(PIDInput input, PIDOutput output, double kP, double kI, double kD, double kF)
    {
        this.input = input;
        this.output = output;

        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    /**
     * A constructor which passes a given motorGroup as source and output, zeros all the gains,
     * and initializes an empty array for ffTerms.
     *
     * @param motorGroup The motorGroup that this PIDFController should control
     */
    public PIDFController(MotorGroup motorGroup)
    {
        this(motorGroup, motorGroup, 0, 0, 0, 0);
    }

    /**
     * This method only exists for unit testing purposes. It evaluates the motion profile and calculates then writes
     * the PIDF output
     */
    protected void execute()
    {
        if (!enabled) return;

        if (kP == 0 && kI == 0 && kD == 0 && kF == 0)
        {
            Logger.getInstance().warn("PIDF gains not configured", this.getClass());
            return;
        }

        double target = setpoint;

        if (motionProfile != null)
        {
            double time = Math.max((System.currentTimeMillis() / 1000.0) - profileStartTime, 0);

            if (time > motionProfile.getDuration()) motionProfile = null;
            else target = motionProfile.evaluate(Math.min(time, motionProfile.getDuration()));
        }
        output.pidSet(calculate(target));
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

            try
            {
                Thread.sleep((long) 1000.0 / UPDATE_RATE);
            }
            catch (InterruptedException exception)
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

        double error = setpoint - currentValue; // Current error
        double output = error * kP + kF;

        if(errors.size() > 0 && kI != 0 && kD != 0)
        {
            double dt = (System.currentTimeMillis() / 1000.0) - errors.lastKey();
            double integral = errorIntegral + error * dt;
            double derivative = (error - errors.lastEntry().getValue()) / dt;

            output += kI * integral - kD * derivative;
        }

        errors.put(System.currentTimeMillis() / 1000.0, error);
        while(errors.firstKey() < (System.currentTimeMillis() / 1000.0) - integralMemory)
        {
            errorIntegral -= errors.firstEntry().getValue() * (errors.higherKey(errors.firstKey()) - errors.firstKey());
            errors.remove(errors.firstKey());
        }

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
        setpoint = motionProfile.evaluate(motionProfile.getDuration());
        profileStartTime = System.currentTimeMillis() / 1000.0;
        enable();
    }

    /**
     * Enables the controller and sets the prev variables to prevent timing issues
     */
    public synchronized void enable()
    {
        errors.clear();
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

    public synchronized void setInput(PIDInput input)
    {
        this.input = input;
    }

    public double getSetpoint()
    {
        return setpoint;
    }

    public void setkP(double kP)
    {
        this.kP = kP;
    }

    public double getkP()
    {
        return kP;
    }

    public double getkI()
    {
        return kI;
    }

    public void setkI(double kI)
    {
        this.kI = kI;
    }

    public double getkD()
    {
        return kD;
    }

    public void setkD(double kD)
    {
        this.kD = kD;
    }

    public double getkF()
    {
        return kF;
    }

    public void setkF(double kF)
    {
        this.kF = kF;
    }
}
