package frc.team1983.utilities.control;

public interface PIDInput
{
    double pidGet();

    /**
     * @return An object that will be passed to the feedforward term functions
     */
    Object getFFOperator();
}
