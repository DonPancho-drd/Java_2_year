package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

public class SubCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (args.length != 0) {
            throw new CommandException("SUB command doesn't support arguments");
        }

        double a = context.pop();
        try{
            double b = context.pop();
            context.push(a - b);
        } catch (CommandException e) {
            context.push(a);
            throw new CommandException("Not enough arguments to do subtraction(only one value left in stack)");
        }
    }
}