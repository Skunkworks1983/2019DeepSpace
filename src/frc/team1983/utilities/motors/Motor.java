package frc.team1983.utilities.motors;

public interface Motor
{
    void set(double output);
    void setBrake(boolean brake);
    void setCurrentLimit(int limit);
}
