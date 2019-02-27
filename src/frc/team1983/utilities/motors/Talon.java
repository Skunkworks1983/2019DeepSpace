package frc.team1983.utilities.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.team1983.utilities.sensors.Encoder;

public class Talon extends com.ctre.phoenix.motorcontrol.can.TalonSRX implements Motor, Encoder
{
    public Talon(int port, boolean reversed)
    {
        super(port);
        setInverted(reversed);
    }

    public void set(double output)
    {
        super.set(ControlMode.PercentOutput, output);
    }

    @Override
    public void setBrake(boolean brake)
    {
        setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
    }

    @Override
    public void setCurrentLimit(int limit)
    {
        //todo
    }

    @Override
    public void configure()
    {
        configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    }

    @Override
    public double getPosition()
    {
        return getSelectedSensorPosition();
    }

    @Override
    public double getVelocity()
    {
        return getSelectedSensorVelocity();
    }
}
