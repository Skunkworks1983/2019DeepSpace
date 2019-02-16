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

/**
 * This class represents a system of motors and an encoder
 */
public class Transmission implements PIDInput, PIDOutput
{
    public static ArrayList<Transmission> transmissions = new ArrayList<>();

    protected ArrayList<Motor> motors;
    protected PIDFController controller;

    private double ticksPerInch, encoderOffset; // added to encoder value for manual encoder zeroing

    protected Encoder encoder;
    private final String name; //For logging purposes
    private FeedbackType feedbackType;

    private double movementVelocity = 0; // in
    private double movementAcceleration = 0; // in/s

    private double p = 0, i = 0, d = 0;

    /**
     * Constructor for a transmission with a name, master, encoder, and other motors, regardless
     * of whether or not the motor controllers are Talons or Sparks.
     * @param name The name of this transmission. Is used for logging.
     * @param feedbackType The feedback type that this motor should use in closed loop control
     * @param encoder The encoder of this system. Usually attached to one of the motors.
     * @param master The master motor. This doesn't mean much but it does ensure we always have at least one motor.
     * @param motors An array of the other motors in this system. Can be left out if there is only one motor.
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

        transmissions.add(this);
    }

    /**
     * Constructor for a transmission where the master motor is also the encoder (either a Talon with an encoder plugged in or
     * a NEO with the built-in encoder)
     * @param master A motor with an attached encoder
     */
    public Transmission(String name, FeedbackType feedbackType, Motor master, Motor... motors)
    {
        this(name, feedbackType, (Encoder) master, master, motors);
    }

    /**
     * Constructor for a transmission where the transmission uses an encoder that is plugged directly into the roboRIO
     *
     * @param encoderPort The port that a new DigitalInputEncoder will be attached to.
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
        encoderOffset = -encoder.getPosition();
    }

    /**
     * Stops the controller from running
     */
    public void disableController()
    {
        if(controller != null)
            controller.disable();
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
     * @param inches A position in inches
     * @return The number of ticks for this position
     */
    public double toTicks(double inches)
    {
        return inches * ticksPerInch;
    }

    /**
     * Convert ticks to inches
     * @param ticks A position in ticks
     * @return The number of inches for this position
     */
    public double toInches(double ticks)
    {
        return ticks / ticksPerInch;
    }

    /**
     * Set the motor output in a control mode
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
        }
        else
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

            controller.runMotionProfile(MotionProfile.generateProfile(pidGet(), value, movementVelocity, movementAcceleration, feedbackType));
        }
    }

    /**
     * @param throttle Sets the percent output of the motors
     */
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
        return encoderOffset + encoder.getPosition();
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

    /**
     * @return The name of this transmission (for logging)
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The configured cruise velocity for this transmission
     */
    public double getMovementVelocity()
    {
        return movementVelocity;
    }

    /**
     * @param movementVelocity Sets the cruise velocity for this transmission
     */
    public void setMovementVelocity(double movementVelocity)
    {
        this.movementVelocity = movementVelocity;
    }

    /**
     * @return The configured max acceleration for this transmission
     */
    public double getMovementAcceleration()
    {
        return movementAcceleration;
    }

    /**
     * @param movementAcceleration Sets the max acceleration of this transmission
     */
    public void setMovementAcceleration(double movementAcceleration)
    {
        this.movementAcceleration = movementAcceleration;
    }

    /**
     * @param output For the PIDFController. Just sets the raw percent output.
     */
    @Override
    public void pidWrite(double output)
    {
        setRawThrottle(output);
    }


    /**
     * @return For the PIDFController. Returns position or velocity depending on the configured feedbacktype
     */
    @Override
    public double pidGet()
    {
        return feedbackType == FeedbackType.POSITION ? getPositionInches() : getVelocityInches();
    }

    /**
     * @return For calculating the arbitrary feed forward terms in PIDFController
     */
    @Override
    public double getFeedForwardValue()
    {
        return getVelocityInches();
    }

    public double getP()
    {
        return p;
    }

    public double getI()
    {
        return i;
    }

    public double getD()
    {
        return d;
    }
}
