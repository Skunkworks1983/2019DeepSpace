package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

public interface Motor
{
    void set(double output);
    double get();
    void setBrake(boolean brake);
}
