package frc.team1983;

public class Constants
{
    public static final double EPSILON = 1e-5;

    public static final double DRIVEBASE_INCHES_PER_TICK = (6 * Math.PI) / 1360.0;
    public static final double PATHING_TANGENT_LENGTH = 0;

    public static class MotorMap
    {
        public static class Drivebase
        {
            public static final int LEFT_1 = 0;
            public static final boolean LEFT_1_REVERSED = false;
            public static final int LEFT_2 = 0;
            public static final boolean LEFT_2_REVERSED = false;
            public static final int LEFT_3 = 0;
            public static final boolean LEFT_3_REVERSED = false;

            public static final int RIGHT_1 = 0;
            public static final boolean RIGHT_1_REVERSED = false;
            public static final int RIGHT_2 = 0;
            public static final boolean RIGHT_2_REVERSED = false;
            public static final int RIGHT_3 = 0;
            public static final boolean RIGHT_3_REVERSED = false;
        }
    }

    public static class OI
    {
        public static final double JOY_DEADZONE = 0;
        public static final double JOY_SCALAR = 0;
        public static final double JOY_EXPONENT = 0;

        public static class Map
        {
            public static int LEFT = 0;
            public static int RIGHT = 0;
            public static int PANEL = 0;
        }
    }

    public static class Estimator
    {
        public static final int UPDATE_RATE = 20;
    }
}
