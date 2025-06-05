package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

public class PushCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new CommandException("PUSH command requires exactly one argument");
        }

        double value;

        try {
            value = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            try {
                value = context.getDefine(args[0]);
            } catch (CommandException ce) {
                throw new CommandException("Invalid argument for PUSH: '" + args[0] +
                        "'. It must be a number or defined variable");
            }
        }

        context.push(value);
    }
}