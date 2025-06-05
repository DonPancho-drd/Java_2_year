package calculator.commands;

import calculator.Command;
import calculator.Context;
import calculator.exceptions.CommandException;

import java.util.Stack;

public class RemoveCommand extends Command {
    @Override
    public void execute(Context context, String[] args) throws CommandException {
        validateArguments(args, context);

        if (args.length == 1) {
            removeSingleElement(context, Integer.parseInt(args[0]));
        } else {
            removeRange(context, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }
    }

    private void validateArguments(String[] args, Context context) throws CommandException {
        if (args.length != 1 && args.length != 2) {
            throw new CommandException("REMOVE command requires either one or two arguments");
        }

        try {
            int firstArg = Integer.parseInt(args[0]);
            if (firstArg < 0) {
                throw new CommandException("Index must be non-negative");
            }
            if (firstArg >= context.stackSize()) {
                throw new CommandException("Index out of bounds");
            }

            if (args.length == 2) {
                int secondArg = Integer.parseInt(args[1]);
                if (secondArg < 0) {
                    throw new CommandException("Index must be non-negative");
                }
                if (secondArg > context.stackSize()) {
                    throw new CommandException("Index out of bounds");
                }
                if (firstArg >= secondArg) {
                    throw new CommandException("Start index must be less than end index");
                }
            }
        } catch (NumberFormatException e) {
            throw new CommandException("Arguments must be integer numbers");
        }
    }

    private void removeSingleElement(Context context, int index) throws CommandException {
        Stack<Double> tempStack = new Stack<>();

        for (int i = 0; i < index; i++) {
            tempStack.push(context.pop());
        }
        context.pop();

        while (!tempStack.isEmpty()) {
            context.push(tempStack.pop());
        }
    }

    private void removeRange(Context context, int startIndex, int endIndex) throws CommandException {
        Stack<Double> tempStack = new Stack<>();

        for (int i = 0; i < startIndex; i++) {
            tempStack.push(context.pop());
        }
        for (int i = startIndex; i < endIndex; i++) {
            context.pop();
        }
        while (!tempStack.isEmpty()) {
            context.push(tempStack.pop());
        }
    }
}