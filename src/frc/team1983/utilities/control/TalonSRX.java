package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class TalonSRX extends com.ctre.phoenix.motorcontrol.can.TalonSRX implements Motor
{
    public TalonSRX(int port, boolean reversed)
    {
        super(port);
        setInverted(reversed);
    }

    public double getPositionTicks()
    {
        return getSelectedSensorPosition();
    }

    public double getVelocityTicks()
    {
        return getSelectedSensorVelocity();
    }

    @Override
    public void setBrake(boolean brake)
    {
        setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
    }
}
