package frc.team1983.constants;

public class CollectorConstants // todo: delete
{
    public static final int WRIST_RIGHT = 1234; //TODO: FIX PORTS
    public static final int WRIST_LEFT = 1234;

    public static final boolean WRIST_RIGHT_REVERSED = false;
    public static final boolean WRIST_LEFT_REVERSED = false;


    public static final int INTAKE_RIGHT = 1234;
    public static final int INTAKE_LEFT = 1234;

    public static final boolean INTAKE_RIGHT_REVERSED = false; //TODO: CHECK DIRECTIONALITY
    public static final boolean INTAKE_LEFT_REVERSED = false;

    public static final double COLLECTOR_INTAKE_THROTTLE = -1; //negative bc pulling towards robot
    public static final double COLLECTOR_EXPEL_FAST_THROTTLE = 1; //full speed forward
    public static final double COLLECTOR_EXPEL_SLOW_THROTTLE = .25; //slow speed forward

    public static final double WRIST_ALLOWABLE_ERROR = 1234; //TODO: FIX MAGIC NUMBER
    public static final double WRIST_DEGREES_PER_TICK = 1234;//TODO: THE MATH

    public static class WristGains
    {
        public static final double P = 1;
        public static final double I = 1;
        public static final double D = 1;
        public static final double F = 1;
    }

    public static class WristSetpoints
    {
        public static final double UP = 0;
        public static final double MIDDLE = 45;
        public static final double DOWN = 90; //TODO: CHECK
    }
}
