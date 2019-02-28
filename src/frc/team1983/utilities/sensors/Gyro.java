package frc.team1983.utilities.sensors;

public interface Gyro
{
    double getHeading();
    float getRoll();
    float getPitch();
    void setHeading(double heading);
    void setRoll(float roll);
    void setPitch(float pitch);
    void reset();
}
