package frc.team1983.utilities.motors;

import frc.team1983.services.logging.Logger;
import frc.team1983.utilities.control.MotorGroupController;
import frc.team1983.utilities.control.PIDInput;
import frc.team1983.utilities.control.PIDOutput;
import frc.team1983.utilities.motion.MotionProfile;
import frc.team1983.utilities.sensors.DigitalInputEncoder;
import frc.team1983.utilities.sensors.Encoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * This class represents a system of motors and an encoder
 */
public class MotorGroup implements PIDInput, PIDOutput
{
    public static ArrayList<MotorGroup> motorGroups = new ArrayList<>();

    private Object ffOperator;

    protected ArrayList<Motor> motors;
    protected MotorGroupController controller;

    private double conversionRatio = 1;
    private double encoderOffset; // added to encoder setpoint for manual encoder zeroing

    protected Encoder encoder;
    private final String name; //For logging purposes

    private double cruiseVelocity = 0;
    private double movementAcceleration = 0;
    private double setpoint = 0;

    private boolean useVelocity = false;

    /**
     * Constructor for a motorGroup with a name, master, encoder, and other motors, regardless
     * of whether or not the motor controllers are Talons or Sparks.
     *
     * @param name         The name of this motorGroup. Is used for logging.
     * @param encoder      The encoder of this system. Usually attached to one of the motors.
     * @param master       The master motor. This doesn't mean much but it does ensure we always have at least one motor.
     * @param motors       An array of the other motors in this system. Can be left out if there is only one motor.
     */
    protected MotorGroup(String name, Encoder encoder, Motor master, Motor... motors)
    {
        this.name = name;

        this.encoder = encoder;
        this.encoder.configure();

        this.motors = new ArrayList<>();
        this.motors.add(master);
        this.motors.addAll(Arrays.asList(motors));

        motorGroups.add(this);
    }

    /**
     * Constructor for a motorGroup where the master motor is also the encoder (either a Talon with an encoder plugged in or
     * a NEO with the built-in encoder)
     *
     * @param master A motor with an attached encoder
     */
    public MotorGroup(String name, Motor master, Motor... motors)
    {
        this(name, (Encoder) master, master, motors);
    }

    /**
     * Constructor for a motorGroup where the motorGroup uses an encoder that is plugged directly into the roboRIO
     *
     * @param encoderPort The port that a new DigitalInputEncoder will be attached to.
     */
    public MotorGroup(String name, int encoderPort, Motor master, Motor... motors)
    {
        this(name, new DigitalInputEncoder(encoderPort), master, motors);
    }

    /**
     * Sets the neos smart current limit
     *
     * @param limit The current limit
     */
    public void setCurrentLimit(int limit)
    {
        for (Motor motor : motors)
        {
            motor.setCurrentLimit(limit);
        }
    }

    /**
     * Reset the encoder offset to that it reads zero at its current position
     */
    public void zero()
    {
        encoderOffset = -encoder.getPosition();
    }

    /**
     * Creates the controller if it does not already exist
     */
    public void createController()
    {
        // controller defaults to null, so subsystems that never use multi-threaded closed-loop control
        // don't bog down the CPU because their closed-loop controller never gets instantiated. controller
        // is only created when we want to use closed-loop control.
        if (controller == null)
        {
            controller = new MotorGroupController(this);
            controller.start();
        }
    }

    /**
     * Adds a feedforward term to the controller
     */
    public void addFFTerm(Function<Object, Double> feedforward)
    {
        createController();
        controller.addFeedforward(feedforward);
    }

    public void setFFOperator(Object ffOperator)
    {
        this.ffOperator = ffOperator;
    }

    /**
     * Stops the controller from running
     */
    public void disableController()
    {
        if (controller != null)
            controller.disable();
    }

    /**
     * Set the motor output in a control mode
     *
     * @param controlMode The control mode the motor should run in
     * @param value       The setpoint at which the motor should run (%, in, in/s)
     */
    public void set(ControlMode controlMode, double value)
    {
        if (controlMode == ControlMode.Throttle)
        {
            disableController();
            setRawThrottle(value);
            return;
        }

        if(controlMode == ControlMode.Velocity)
        {
            setUseVelocity(true);
        }
        else
        {
            setUseVelocity(false);
        }

        if(value == setpoint) return;
        setpoint = value;

        createController();
        if(controlMode == ControlMode.MotionMagic)
            controller.runMotionProfile(MotionProfile.generateProfile(pidGet(), value, cruiseVelocity, movementAcceleration));
        else controller.setSetpoint(value);

    }

    /**
     * @param throttle Sets the percent output of the motors
     */
    public void setRawThrottle(double throttle)
    {
        for (Motor motor : motors)
            motor.set(throttle);
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
    public void setKP(double kP)
    {
        createController();
        controller.setKP(kP);
    }

    /**
     * Sets the PID gains of the controller
     */
    public void setKD(double kD)
    {
        createController();
        controller.setKD(kD);
    }

    public double getSetpoint()
    {
        return setpoint;
    }

    /**
     * @return Get the current position in encoder ticks
     */
    public double getPositionTicks()
    {
        return encoderOffset + encoder.getPosition();
    }

    /**
     * @return Current encoder ticks per second
     */
    public double getVelocityTicks()
    {
        return encoder.getVelocity();
    }

    /**
     * @return Get the current position
     */
    public double getPosition()
    {
        return getPositionTicks() * conversionRatio;
    }

    /**
     * @return Current encoder velocity
     */
    public double getVelocity()
    {
        return getVelocityTicks() * conversionRatio;
    }

    /**
     * @return The name of this motorGroup (for logging)
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The configured cruise velocity for this motorGroup
     */
    public double getCruiseVelocity()
    {
        return cruiseVelocity;
    }

    /**
     * @param cruiseVelocity Sets the cruise velocity for this motorGroup
     */
    public void setCruiseVelocity(double cruiseVelocity)
    {
        this.cruiseVelocity = cruiseVelocity;
    }

    /**
     * @return The configured max acceleration for this motorGroup
     */
    public double getMovementAcceleration()
    {
        return movementAcceleration;
    }

    /**
     * @param movementAcceleration Sets the max acceleration of this motorGroup
     */
    public void setMovementAcceleration(double movementAcceleration)
    {
        this.movementAcceleration = movementAcceleration;
    }

    /**
     * @param output For the MotorGroupController. Just sets the raw percent output.
     */
    @Override
    public void pidWrite(double output)
    {
        setRawThrottle(output);
    }

    public void setUseVelocity(boolean useVelocity)
    {
        this.useVelocity = useVelocity;
    }

    /**
     * @return For the MotorGroupController. Returns position or velocity depending on the configured feedbacktype
     */
    @Override
    public double pidGet()
    {
        return useVelocity ? getVelocity() : getPosition();
    }

    /**
     * @return For calculating the arbitrary feed forward terms in MotorGroupController
     */
    @Override
    public Object getFFOperator()
    {
        return ffOperator;
    }

    public double getP()
    {
        return controller.getKP();
    }


    public void follow(MotorGroup leader)
    {
        createController();
        controller.follow(leader);
    }

    public void enableController()
    {
        controller.enable();
    }

    public double getConversionRatio()
    {
        return conversionRatio;
    }

    public void setConversionRatio(double conversionRatio)
    {
        this.conversionRatio = conversionRatio;
    }

    public boolean controllerIsEnabled()
    {
        return controller.isEnabled();
    }
}
