package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

public class SqrtCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (args.length != 0) {
            throw new CommandException("SQRT command doesn't support arguments");
        }
        double a = context.pop();
        if (a < 0){
            context.push(a);
            throw new CommandException("Value for SQRT can not be negative");
        }
        double result = Math.sqrt(a);
        context.push(result);
    }
}