package frc.team1983.utilities.motors;

public enum ControlMode
{
    Throttle(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput),
    Position(com.ctre.phoenix.motorcontrol.ControlMode.MotionMagic),
    Velocity(com.ctre.phoenix.motorcontrol.ControlMode.Velocity);

    public final com.ctre.phoenix.motorcontrol.ControlMode TALON;
    public final FeedbackType feedbackType;

    ControlMode(com.ctre.phoenix.motorcontrol.ControlMode TALON)
    {
        this.TALON = TALON;

        switch(this)
        {
            case Throttle:
                feedbackType = FeedbackType.NONE;
                break;
            case Position:
                feedbackType = FeedbackType.POSITION;
                break;
            default:
                feedbackType = FeedbackType.VELOCITY;
        }
    }
}
