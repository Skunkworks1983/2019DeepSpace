package frc.team1983.utilities.motors;

public enum ControlMode
{
    PERCENT_OUTPUT(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput);

    public final com.ctre.phoenix.motorcontrol.ControlMode TALON;
    ControlMode(com.ctre.phoenix.motorcontrol.ControlMode TALON)
    {
        this.TALON = TALON;
    }
}
