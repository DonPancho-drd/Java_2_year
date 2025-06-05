package calculator;

import calculator.exceptions.CommandException;

public abstract class Command {
    public abstract void execute(Context context, String[] args) throws CommandException;
}