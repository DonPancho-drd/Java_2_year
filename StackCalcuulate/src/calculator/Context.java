package calculator;

import calculator.exceptions.CommandException;

import java.util.Stack;
import java.util.HashMap;

public class Context {
    private final Stack<Double> stack = new Stack<>();
    private final HashMap<String, Double> defines = new HashMap<>();

    public void push(double value) {
        stack.push(value);
    }

    public double pop() throws CommandException {
        if (stack.isEmpty()) {
            throw new CommandException("Stack is empty");
        }
        return stack.pop();
    }

    public double peek() throws CommandException {
        if (stack.isEmpty()) {
            throw new CommandException("Stack is empty");
        }
        return stack.peek();
    }

    public void define(String key, double value) {
        defines.put(key, value);
    }
    public int stackSize() {
        return stack.size();
    }

    public double getDefine(String key) throws CommandException {
        if (!defines.containsKey(key)) {
            throw new CommandException("Undefined variable: " + key);
        }
        return defines.get(key);
    }
}