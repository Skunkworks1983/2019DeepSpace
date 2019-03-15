package frc.team1983.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

import java.util.function.Function;

public class ConditionalCommand extends edu.wpi.first.wpilibj.command.ConditionalCommand
{
    private Function<Void, Boolean> condition;

    public ConditionalCommand(Command onTrue, Command onFalse, Function<Void, Boolean> condition)
    {
        super(onTrue, onFalse);
        this.condition = condition;
    }

    public ConditionalCommand(Command onTrue, Function<Void, Boolean> condition)
    {
        this(onTrue, null, condition);
    }

    @Override
    protected boolean condition()
    {
        return condition.apply(null);
    }
}
