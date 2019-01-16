package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;

import java.util.Map;

import static java.lang.Math.abs;

public class OI
{
    public static final double JOY_DEADZONE = 0.1;
    public static final double SLIDER_SCALAR = 0.618726; //Scalar coefficient of the slider on the OIMap

    private Map<Joysticks, JoystickWrapper> joyMap;

    public OI()
    {
        joyMap = Map.of(
                Joysticks.LEFT, new JoystickWrapper(Joysticks.LEFT.ordinal()),
                Joysticks.RIGHT, new JoystickWrapper(Joysticks.RIGHT.ordinal()),
                Joysticks.PANEL, new JoystickWrapper(Joysticks.PANEL.ordinal()));
    }

    public double getRawAxis(Joysticks joystick, Joystick.AxisType axis)
    {
        return joyMap.get(joystick).getRawAxis(axis.value);
    }

    public double getAxis(Joysticks joystick, Joystick.AxisType axis)
    {
        double raw = getRawAxis(joystick, axis);
        return abs(raw) < JOY_DEADZONE ? 0 : raw * abs(raw);
    }

    //The 2017 slider was a joystick axis. All code taken from 2018 (which was taken from 2017)
    public double getElevatorSliderPos()
    {
        double x = getRawAxis(Joysticks.PANEL, Joystick.AxisType.kX); //kX is 0
        x = Math.pow(x, 10);
        x = x / SLIDER_SCALAR;
        x = 1 - x;
        return x;
    }

    public enum Joysticks //ordinal is used, so order matters
    {
        LEFT,
        RIGHT,
        PANEL,
    }
}
