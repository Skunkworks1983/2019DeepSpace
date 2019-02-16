package frc.team1983.utilities.motors;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.control.PIDFController;
import frc.team1983.utilities.control.PIDInput;
import frc.team1983.utilities.control.PIDOutput;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.sensors.DigitalInputEncoder;
import frc.team1983.utilities.sensors.Encoder;

import java.util.ArrayList;
import java.util.Arrays;

public class Transmission implements PIDInput, PIDOutput
{
    private ArrayList<Motor> motors;
    private PIDFController controller;

    private double ticksPerInch, encoderOffset; // added to encoder value for manual encoder zeroing

    private Encoder encoder;
    private final String name; //For logging purposes
    private FeedbackType feedbackType;

    private double movementVelocity = 0; // in
    private double movementAcceleration = 0; // in/s

    private double p = 0, i = 0, d = 0;

    /**
     * Constructor for a transmission with a name, master, encoder, and other motors, regardless
     * of whether or not the motor controllers are Talons or Sparks
     *
     * @param name
     * @param master
     * @param encoder
     * @param motors
     */
    protected Transmission(String name, FeedbackType feedbackType, Encoder encoder, Motor master, Motor... motors)
    {
        this.name = name;

        this.feedbackType = feedbackType;

        this.encoder = encoder;
        this.encoder.configure();

        this.motors = new ArrayList<>();
        this.motors.add(master);
        this.motors.addAll(Arrays.asList(motors));
    }

    /**
     * Constructor for a transmission where the master motor is also the encoder (either a Talon with an encoder plugged in or
     * a NEO with the built-in encoder)
     *
     * @param name
     * @param master
     * @param motors
     */
    public Transmission(String name, FeedbackType feedbackType, Motor master, Motor... motors)
    {
        this(name, feedbackType, (Encoder) master, master, motors);
    }

    /**
     * Constructor for a transmission where the transmission uses an encoder that is plugged directly into the roboRIO
     *
     * @param name
     * @param master
     * @param encoderPort
     * @param motors
     */
    public Transmission(String name, FeedbackType feedbackType, int encoderPort, Motor master, Motor... motors)
    {
        this(name, feedbackType, new DigitalInputEncoder(encoderPort), master, motors);
    }

    /**
     * Reset the encoder offset to that it reads zero at its current position
     */
    public void zero()
    {
        encoderOffset = -getPositionTicks();
    }

    /**
     * @param ticksPerInch The new ticksPerInch
     */
    public void setTicksPerInch(double ticksPerInch)
    {
        this.ticksPerInch = ticksPerInch;
    }

    /**
     * Convert inches to ticks
     *
     * @param inches A position in inches
     * @return The number of ticks for this position
     */
    public double toTicks(double inches)
    {
        return inches * ticksPerInch;
    }

    /**
     * Convert ticks to inches
     *
     * @param ticks A position in ticks
     * @return The number of inches for this position
     */
    public double toInches(double ticks)
    {
        return ticks / ticksPerInch;
    }

    /**
     * Set the motor output in a control mode
     *
     * @param controlMode The control mode the motor should run in
     * @param value       The value at which the motor should run (%, in, in/s)
     */
    public void set(ControlMode controlMode, double value)
    {
        if (controlMode == ControlMode.Throttle)
        {
            if(controller != null)
                controller.disable();
            setRawThrottle(value);
        } else
        {
            // controller defaults to null, so subsystems that never use multi-threaded closed-loop control
            // don't bog down the CPU because their closed-loop controller never gets instantiated. controller
            // is only created when we want to use closed-loop control.
            if (controller == null)
            {
                controller = new PIDFController(this);
                controller.setPID(p, i, d);
                controller.start();
            }

            if (movementVelocity == 0 || movementAcceleration == 0)
                Logger.getInstance().warn("movement acceleration or velocity not configured", this.getClass());

            feedbackType = controlMode == ControlMode.Position ? FeedbackType.POSITION : FeedbackType.VELOCITY;

            controller.runMotionProfile(MotionProfile.generateProfile(
                    pidGet(), value, movementVelocity, movementAcceleration, feedbackType));
        }
    }

    public void setRawThrottle(double throttle)
    {
        for (Motor motor : motors)
            motor.set(ControlMode.Throttle, throttle);
    }

    /**
     * @param brake If the motors should be in brake mode
     */
    public void setBrake(boolean brake)
    {
        for (Motor motor : motors)
            motor.setBrake(brake);
    }

    /**
     * Sets the PID gains of the controller
     *
     * @param p
     * @param i
     * @param d
     */
    public void setPID(double p, double i, double d)
    {
        this.p = p;
        this.i = i;
        this.d = d;
        if(controller != null) controller.setPID(p, i, d);
    }

    /**
     * @return Get the current position in encoder ticks
     */
    public double getPositionTicks()
    {
        return encoder.getPosition();
    }

    /**
     * @return Get current position in inches
     */
    public double getPositionInches()
    {
        return toInches(getPositionTicks());
    }

    /**
     * @return Current encoder ticks per second
     */
    public double getVelocityTicks()
    {
        return encoder.getVelocity();
    }

    /**
     * @return getVelocityInInchesPerSecond
     */
    public double getVelocityInches()
    {
        return toInches(getVelocityTicks());
    }

    public String getName()
    {
        return name;
    }

    public double getMovementVelocity()
    {
        return movementVelocity;
    }

    public void setMovementVelocity(double movementVelocity)
    {
        this.movementVelocity = movementVelocity;
    }

    public double getMovementAcceleration()
    {
        return movementAcceleration;
    }

    public void setMovementAcceleration(double movementAcceleration)
    {
        this.movementAcceleration = movementAcceleration;
    }

    @Override
    public void pidWrite(double output)
    {
        setRawThrottle(output);
    }


    @Override
    public double pidGet()
    {
        return feedbackType == FeedbackType.POSITION ? getPositionTicks() : getVelocityTicks();
    }

    @Override
    public double getFeedForwardValue()
    {
        return getVelocityInches();
    }
}
