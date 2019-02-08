package frc.team1983.utilities.sensors;

public interface Encoder
{
    void configure();
    int getPosition();
    double getVelocity();
}
