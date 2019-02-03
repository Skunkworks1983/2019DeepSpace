package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.SpeedController;

import java.util.ArrayList;
import java.util.Arrays;

public class Transmission {
    private ArrayList<Motor> motors;
    private Profiler profiler;

    private double ticksToInches = 1; // sensor u/in
    private double encoderOffset = 0; // added to encoder value for manual encoder zeroing

    public Transmission(Motor master, Motor... motors) {
        this.motors = new ArrayList<>(Arrays.asList(motors));

        for(int i = 0; i < motors.length; i++)
            this.motors.add(i + 1, motors[i]);
        this.motors.add(0, master);

        if(master instanceof TalonSRX)
            ((TalonSRX) master).configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    }

    public void zero()
    {
        encoderOffset = -getPositionTicks();
    }

    public void setTicksToInches(double ticksToInches)
    {
        this.ticksToInches = ticksToInches;
    }

    public double toTicks(double inches)
    {
        return inches * ticksToInches;
    }

    public double toInches(double ticks)
    {
        return ticks / ticksToInches;
    }

    public void set(ControlMode mode, double value)
    {
        for(Motor motor : motors)
            motor.set(mode, value);
    }

    public void setBrake(boolean brake)
    {
        for(Motor motor : motors)
            motor.setBrake(brake);
    }

    // sensor u
    public double getPositionTicks()
    {
        return motors.get(0).getPositionTicks() + encoderOffset;
    }

    // in
    public double getPositionInches()
    {
        return toInches(getPositionTicks());
    }

    // sensor u/s
    public double getVelocityTicks()
    {
        return motors.get(0).getVelocityTicks();
    }

    // in/s
    public double getVelocityInches()
    {
        return toInches(getVelocityTicks());
    }
}