package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

public class DivCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (args.length != 0) {
            throw new CommandException("DIV command doesn't support arguments");
        }

        double a = context.pop();
        try{
            double b = context.pop();

            if (b == 0.0){
                throw new CommandException("Division by zero");
            }
            context.push(b / a);
        } catch (CommandException e) {
            context.push(a);
            throw new CommandException("Not enough arguments to do division(only one value left in stack)");
        }
    }
}