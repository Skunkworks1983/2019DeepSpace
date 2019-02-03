package frc.team1983.utilities.control;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class TalonSRX extends com.ctre.phoenix.motorcontrol.can.TalonSRX implements Motor
{
    public TalonSRX(int port, boolean reversed)
    {
        super(port);
        setInverted(reversed);
    }

    @Override
    public void setBrake(boolean brake)
    {
        setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
    }

    @Override
    public void set(double output)
    {

    }

    @Override
    public double get()
    {
        return 0;
    }
}
