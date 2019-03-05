package frc.team1983.commands;

import edu.wpi.first.wpilibj.command.Command;

public class CommandGroup extends edu.wpi.first.wpilibj.command.CommandGroup
{
    public CommandGroup(Command... commands)
    {
        for(Command command : commands)
            addParallel(command);
    }
}
