package frc.team1983.constants;

public class RobotMap
{
    public static final int COMPRESSOR = 7;

    public static class Drivebase
    {
        public static final int LEFT_1 = 12;
        public static final boolean LEFT_1_REVERSED = false;
        public static final int LEFT_2 = 13;
        public static final boolean LEFT_2_REVERSED = false;
        public static final int LEFT_3 = 14;
        public static final boolean LEFT_3_REVERSED = false;

        public static final int RIGHT_1 = 1;
        public static final boolean RIGHT_1_REVERSED = true;
        public static final int RIGHT_2 = 2;
        public static final boolean RIGHT_2_REVERSED = true;
        public static final int RIGHT_3 = 3;
        public static final boolean RIGHT_3_REVERSED = true;
    }

    public static class Collector
    {
        public static final int LEFT = 15;
        public static final boolean LEFT_REVERSED = false;

        public static final int RIGHT = 16;
        public static final boolean RIGHT_REVERSED = true;

        public static final int ROLLER = 5;
        public static final boolean ROLLER_REVERSED = false;

        public static final int PISTON_FORWARD = 0;
        public static final int PISTON_REVERSE = 1;
    }

    public static class Manipulator
    {
        public static final int HOOKS_FORWARD = 2;
        public static final int HOOKS_REVERSE = 3;

        public static final int EXTENDER_FORWARD = 4;
        public static final int EXTENDER_REVERSE = 5;

        public static final int LEFT_GRIPPER = 10;
        public static final boolean LEFT_GRIPPER_REVERSED = false;
        public static final int RIGHT_GRIPPER = 9;
        public static final boolean RIGHT_GRIPPER_REVERSED = false;
    }

    public static class Elevator
    {
        public static final int LEFT = 11;
        public static final boolean LEFT_REVERSED = true;

        public static final int RIGHT = 4;
        public static final boolean RIGHT_REVERSED = false;
    }

    public static class Climber
    {
        public static final int RIGHT = 6;
        public static final boolean RIGHT_REVERSED = false;
    }
}
