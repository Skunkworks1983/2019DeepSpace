package frc.team1983.utilities.control;

import frc.team1983.utilities.motors.FeedbackType;

public interface PIDSource
{
    double pidGet(FeedbackType feedbackType);
}
