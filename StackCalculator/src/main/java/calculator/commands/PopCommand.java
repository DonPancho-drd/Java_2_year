package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

public class PopCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (args.length != 0) {
            throw new CommandException("POP command doesn't support arguments");
        }
        context.pop();
    }
}
