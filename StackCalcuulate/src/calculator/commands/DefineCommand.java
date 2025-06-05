package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

public class DefineCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        // Проверка количества аргументов
        if (args.length != 2) {
            throw new CommandException("DEFINE command requires exactly 2 arguments");
        }

        String name = args[0];
        double value;

        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid number format: " + args[1]);
        }

        if (!isValidVariableName(name)) {
            throw new CommandException("Invalid variable name: " + name);
        }

        context.define(name, value);
    }

    private boolean isValidVariableName(String name) {
        return name.matches("[a-zA-Z][a-zA-Z0-9]*");
    }
}