package frc.team1983.utilities.motors;

public enum ControlMode
{
    Throttle(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput),
    Position(com.ctre.phoenix.motorcontrol.ControlMode.MotionMagic),
    Velocity(com.ctre.phoenix.motorcontrol.ControlMode.Velocity);

    public final com.ctre.phoenix.motorcontrol.ControlMode TALON;

    ControlMode(com.ctre.phoenix.motorcontrol.ControlMode TALON)
    {
        this.TALON = TALON;
    }
}
