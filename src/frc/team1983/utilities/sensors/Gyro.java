package frc.team1983.utilities.sensors;

public interface Gyro
{
    double getHeading();
    void setHeading(double heading);
    void reset();
}
