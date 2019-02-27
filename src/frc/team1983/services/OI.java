package frc.team1983.services;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.team1983.Robot;
import frc.team1983.commands.climber.ClimbLevelTwo;
import frc.team1983.commands.climber.ManualClimber;
import frc.team1983.commands.collector.SetCollectorAngle;
import frc.team1983.commands.collector.SetCollectorRollerThrottle;
import frc.team1983.commands.collector.SetCollectorWristThrottle;
import frc.team1983.commands.collector.ToggleCollector;
import frc.team1983.commands.elevator.IncrementElevatorPosition;
import frc.team1983.commands.elevator.ManualElevator;
import frc.team1983.commands.elevator.SetElevatorPosition;
import frc.team1983.commands.manipulator.SetManipulatorExtended;
import frc.team1983.commands.manipulator.SetManipulatorRollerSpeed;
import frc.team1983.commands.manipulator.ToggleExtender;
import frc.team1983.commands.manipulator.ToggleHooks;
import frc.team1983.commands.climber.ClimbLevelThree;
import frc.team1983.subsystems.Elevator;

import java.util.HashMap;
public class OI
{
    public enum Joysticks
    {
        LEFT(0),
        RIGHT(1),
        PANEL(2);

        private int port;
        Joysticks(int port)
        {
            this.port = port;
        }

        public int getPort()
        {
            return port;
        }
    }

    protected static final double JOYSTICK_DEADZONE = 0.15;
    protected static final double JOYSTICK_EXPONENT = 3;

    private Joystick left, right, panel;
    private HashMap<Joysticks, HashMap<Integer, JoystickButton>> buttons;

    public OI(Joystick left, Joystick right, Joystick panel, HashMap<Joysticks, HashMap<Integer, JoystickButton>> buttons)
    {
        this.left = left;
        this.right = right;
        this.panel = panel;
        this.buttons = buttons;
    }

    public OI()
    {
        this(new Joystick(Joysticks.LEFT.getPort()),
                new Joystick(Joysticks.RIGHT.getPort()),
                new Joystick(Joysticks.PANEL.getPort()),
                new HashMap<>()
        );
    }

    protected static double scale(double raw)
    {
        double deadzoned = Math.abs(raw) > JOYSTICK_DEADZONE ? raw : 0;
        return Math.pow(Math.abs(deadzoned), JOYSTICK_EXPONENT) * Math.signum(deadzoned);
    }

    public double getLeftY()
    {
        return scale(-left.getY());
    }

    public double getRightY()
    {
        return scale(-right.getY());
    }

    public JoystickButton getButton(Joysticks joystickPort, int button)
    {
        Joystick joystick;
        switch(joystickPort)
        {
            case LEFT:
                joystick = left;
                break;
            case RIGHT:
                joystick = right;
                break;
            default: // If it wasn't the other two it must be panel. Java doesn't like it if we just do case PANEL.
                joystick = panel;
                break;
        }

        if(!buttons.containsKey(joystickPort))
            buttons.put(joystickPort, new HashMap<>());
        if(!buttons.get(joystickPort).containsKey(button))
            buttons.get(joystickPort).put(button, new JoystickButton(joystick, button));

        return buttons.get(joystickPort).get(button);
    }

    public void initializeBindings()
    {
        //Button to switch to manual mode is 24
        //Button to switch between balls and hatches is 14
        //Extra buttons are 17, 18, 19
      
        //Controls for pneumatics
        getButton(Joysticks.PANEL,7).whenPressed(new ToggleCollector());
        getButton(Joysticks.PANEL,23).whenPressed(new SetManipulatorExtended(true));
        getButton(Joysticks.PANEL,20).whenPressed(new SetManipulatorExtended(false));
        getButton(Joysticks.PANEL,8).whenPressed(new ToggleHooks());

        //Expel
        getButton(Joysticks.PANEL,22).whileHeld(new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),1,1,true));

        //Intake
        getButton(Joysticks.PANEL,21).whileHeld(new SetManipulatorRollerSpeed(Robot.getInstance().getManipulator(),-0.8,-0.8,true));
        //getButton(Joysticks.PANEL,21).whileHeld(new SetCollectorRollerThrottle(1));

        //Manual collector wrist control
        getButton(Joysticks.PANEL,5).whileHeld(new SetCollectorWristThrottle(0.5));
        getButton(Joysticks.PANEL,6).whileHeld(new SetCollectorWristThrottle(-0.25));


        //Example
//        getButton(Joysticks.PANEL, 10).whenPressed(new ConditionalCommand(
//                new SetElevatorPosition(Elevator.TOP_HATCH),
//                new SetElevatorPosition(Elevator.ROCKET_TOP_BALL)
//        )
//        {
//            @Override
//            protected boolean condition()
//            {
//                return Robot.getInstance().getElevator().getMode();
//            }
//        });

        //Bottom
        getButton(Joysticks.PANEL,16).whenPressed(new SetElevatorPosition(Elevator.BOTTOM));

        //Loading station ball
        getButton(Joysticks.PANEL,15).whenPressed(new SetElevatorPosition(Elevator.FEEDER_BALL));

        //Ball cargo ship
        getButton(Joysticks.PANEL,12).whenPressed(new SetElevatorPosition(Elevator.CARGOSHIP_BALL));

        //low hatch/ball rocket
        getButton(Joysticks.PANEL,11).whenPressed(new SetElevatorPosition(Elevator.ROCKET_LOW_BALL));

        //middle hatch/ball
        getButton(Joysticks.PANEL,10).whenPressed(new SetElevatorPosition(Elevator.ROCKET_MIDDLE_BALL));

        //Top hatch/ball
        getButton(Joysticks.PANEL,9).whenPressed(new SetElevatorPosition(Elevator.ROCKET_TOP_BALL));


        //manual climb elevator up
        getButton(Joysticks.PANEL,4).whileHeld(new ManualClimber(0.5));

        //manual climb elevator up
        getButton(Joysticks.PANEL,3).whileHeld(new ManualClimber(-0.5));

        //manual elevator up
        getButton(Joysticks.PANEL,2).whileHeld(new ManualElevator(0.5));

        //manual elevator down
        getButton(Joysticks.PANEL,1).whileHeld(new ManualElevator(-0.5));

        getButton(Joysticks.PANEL, 13).whileHeld(new ConditionalCommand(
                new ClimbLevelTwo(),
                new ClimbLevelThree()
        )
        {
            @Override
            protected boolean condition()
            {
                return getButton(Joysticks.PANEL,25).get();
            }
        });


//        getButton(Joysticks.LEFT, 1).whenPressed(new IncrementElevatorPosition(3));
//        getButton(Joysticks.LEFT, 2).whenPressed(new IncrementElevatorPosition(-3));
    }
}
