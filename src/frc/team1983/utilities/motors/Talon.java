package frc.team1983.utilities.motors;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Talon extends com.ctre.phoenix.motorcontrol.can.TalonSRX implements Motor, Encoder
{
    public Talon(int port, boolean reversed)
    {
        super(port);
        setInverted(reversed);
    }

    @Override
    public void set(ControlMode mode, double output)
    {
        super.set(mode.TALON, output);
    }

    @Override
    public void setBrake(boolean brake)
    {
        setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
    }

    @Override
    public int getPos()
    {
        return getSelectedSensorPosition();
    }

    @Override
    public double getVel()
    {
        return getSelectedSensorVelocity();
    }
}
