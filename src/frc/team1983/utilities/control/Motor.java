package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.ControlMode;

public interface Motor
{
    void set(ControlMode mode, double output);
    double getPositionTicks();
    double getVelocityTicks();
    void setBrake(boolean brake);
}
