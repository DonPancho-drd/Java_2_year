package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

public class PrintCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (args.length != 0) {
            throw new CommandException("PRINT command doesn't support arguments");
        }
        double a = context.peek();
        System.out.println(a);
    }
}