package frc.team1983.utilities.motors;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.control.TransmissionController;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.sensors.DigitalInputEncoder;
import frc.team1983.utilities.sensors.Encoder;


import java.util.ArrayList;
import java.util.Arrays;

public class Transmission
{
    private ArrayList<Motor> motors;
    private TransmissionController controller;

    private double ticksPerInch, encoderOffset; // added to encoder value for manual encoder zeroing

    private Encoder encoder;
    private final String name; //For logging purposes

    private double movementVelocity = 0; // in
    private double movementAcceleration = 0; // in/s

    /**
     * Constructor for a transmission with a name, master, encoder, and other motors, regardless
     * of whether or not the motor controllers are Talons or Sparks
     *
     * @param name
     * @param master
     * @param encoder
     * @param motors
     */
    protected Transmission(String name, Encoder encoder, Motor master, Motor... motors)
    {
        this.name = name;

        this.encoder = encoder;
        this.encoder.configure();

        this.controller = new TransmissionController(this, FeedbackType.POSITION);

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
    public Transmission(String name, Motor master, Motor... motors)
    {
        this(name, (Encoder) master, master, motors);
    }

    /**
     * Constructor for a transmission where the transmission uses an encoder that is plugged directly into the roboRIO
     *
     * @param name
     * @param master
     * @param encoderPort
     * @param motors
     */
    public Transmission(String name, Motor master, int encoderPort, Motor... motors)
    {
        this(name, new DigitalInputEncoder(encoderPort), master, motors);
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
        if(controlMode == ControlMode.Throttle)
        {
            controller.disable();

            for(Motor motor : motors)
                motor.set(controlMode, value);
        }
        else
        {
            if(movementVelocity == 0 || movementAcceleration == 0)
                Logger.getInstance().warn("movement acceleration or velocity not configured", this.getClass());

            MotionProfile profile = MotionProfile.generateTrapezoidalProfile(getPositionInches(), value, movementVelocity, movementAcceleration);
            controller.runMotionProfile(profile);
            controller.enable();
        }
    }

    /**
     * @param brake If the motors should be in brake mode
     */
    public void setBrake(boolean brake)
    {
        for(Motor motor : motors)
            motor.setBrake(brake);
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
    public double getTicksPerSecond()
    {
        return encoder.getVelocity();
    }

    /**
     * @return getVelocityInInchesPerSecond
     */
    public double getInchesPerSecond()
    {
        return toInches(getTicksPerSecond());
    }

    public String getName()
    {
        return name;
    }

    public void setMovementVelocity(double movementVelocity)
    {
        this.movementVelocity = movementVelocity;
    }

    public void setMovementAcceleration(double movementAcceleration)
    {
        this.movementAcceleration = movementAcceleration;
    }
}
