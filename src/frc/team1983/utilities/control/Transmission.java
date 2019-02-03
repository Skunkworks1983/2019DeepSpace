package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;

public class Transmission {
    private Motor[] motors;
    private Profiler profiler;

    private double reduction = 1;

    private double encoderResolution = 0; // ticks/rev
    private double encoderOffset = 0; // added to encoder value for manual encoder zeroing

    public Transmission(Motor master, Motor... motors) {
        for(int i = 0; i < motors.length; i++)
            motors[i + 1] = motors[i];
        motors[0] = master;

        this.motors = motors;
    }

    public void setReduction(double reduction)
    {
        this.reduction = reduction;
    }

    public void zero()
    {

    }

    public void set(ControlMode mode, double value) {
        for(Motor motor : motors)
            motor.set(value);
    }

    public void setBrake(boolean brake)
    {
        for(Motor motor : motors)
            motor.setBrake(brake);
    }

    // sensor u
    public double getPositionTicks()
    {
        return 0;
    }

    // in
    public double getPositionInches()
    {
        return 0;
    }

    // in/s
    public double getVelocityInches()
    {
        return 0;
    }

    // sensor u/s
    public double getVelocityTicks()
    {
        return 0;
    }
}