package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;

public class Transmission {
    private Motor[] motors;
    private Profiler profiler;

    private double encoderOffset = 0;

    public Transmission(Motor master, Motor... motors) {
        for(int i = 0; i < motors.length; i++)
            motors[i + 1] = motors[i];
        motors[0] = master;

        this.motors = motors;
    }

    public void set(double throttle) {
        for(Motor motor : motors)
            motor.set(throttle);
    }
}