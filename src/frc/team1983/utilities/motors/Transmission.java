package frc.team1983.utilities.motors;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import java.util.ArrayList;
import java.util.Arrays;

public class Transmission
{
    private ArrayList<Motor> motors;

    private double ticksPerInch;
    private double encoderOffset; // added to encoder value for manual encoder zeroing

    private Encoder encoder;

    /**
     * The constructor for using TalonSRXs
     * @param master The motor controller with the encoder
     * @param moreMotors Any more controllers that this transmission should have
     */
    public Transmission(Talon master, Motor... moreMotors) {
        motors = new ArrayList<>();
        motors.add(master);
        motors.addAll(Arrays.asList(moreMotors));

        master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        encoder = master;
    }

    /**
     * The constructor for using CANSparkMaxes
     * @param encoderPort The encoder port
     * @param master The motor controller with the encoder
     * @param moreMotors Any more controllers that this transmission should have
     */
    public Transmission(int encoderPort, SparkMax master, Motor... moreMotors)
    {
        ThreadedEncoder encoder = new ThreadedEncoder(encoderPort);
        encoder.start();
        this.encoder = encoder;

        motors = new ArrayList<>();
        motors.add(master);
        motors.addAll(Arrays.asList(moreMotors));
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
     * @param value The value at which the motor should run
     */
    public void set(ControlMode controlMode, double value)
    {
        for(Motor motor : motors)
            motor.set(controlMode, value);
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
        return encoder.getPos();
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
        return encoder.getVel();
    }

    /**
     * @return getVelocityInInchesPerSecond
     */
    public double getInchesPerSecond()
    {
        return toInches(getTicksPerSecond());
    }
}
