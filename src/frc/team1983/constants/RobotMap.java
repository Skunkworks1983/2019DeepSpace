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

    public static class Collector // todo: rename
    {
        public static final int LEFT = 0; // todo
        public static final boolean LEFT_REVERSED = false; // todo

        public static final int RIGHT = 0; // todo
        public static final boolean RIGHT_REVERSED = false; // todo
    }

    public static class Manipulator
    {
        public static final int HOOKS_FORWARD = 0;
        public static final int HOOKS_REVERSE = 0;

        public static final int EXTENDER_FORWARD = 0;
        public static final int EXTENDER_REVERSE = 0;

        public static final int LEFT_GRIPPER = 0;
        public static final boolean LEFT_GRIPPER_REVERSED = false;
        public static final int RIGHT_GRIPPER = 0;
        public static final boolean RIGHT_GRIPPER_REVERSED = false;
    }

    public static class Elevator //todo: rename
    {
        public static final int LEFT = 0; //todo: find actual value
        public static final boolean LEFT_REVERSED = false; //todo: change

        public static final int RIGHT = 1; //todo: find actual value
        public static final boolean RIGHT_REVERSED = false; // todo: change
    }
}
