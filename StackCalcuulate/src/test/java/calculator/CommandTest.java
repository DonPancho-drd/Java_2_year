package test.java.calculator;

import calculator.CommandFactory;
import calculator.Context;
import calculator.exceptions.CommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CommandTest {
    private Context context;
    private CommandFactory factory;

    @BeforeEach
    void setUp() throws CommandException {
        context = new Context();
        factory = new CommandFactory();
    }
    //Mockito

    @Test
    void testAddCommand() throws CommandException {
        context.push(2.0);
        context.push(3.0);
        factory.getCommand("+").execute(context, new String[]{});
        assertEquals(5.0, context.pop());
    }

    @Test
    void testThrowsAddCommand() throws CommandException {
        context.push(2.0);
        assertThrows(CommandException.class, () -> factory.getCommand("+").execute(context, new String[]{}));
        assertEquals(2.0, context.pop());
    }

    @Test
    void testSubtractCommand() throws CommandException {
        context.push(3.0);
        context.push(5.0);
        factory.getCommand("-").execute(context, new String[]{});
        assertEquals(2.0, context.pop());
    }

    @Test
    void testThrowsSubtractCommand() throws CommandException {
        context.push(2.0);
        assertThrows(CommandException.class, () -> factory.getCommand("-").execute(context, new String[]{}));
        assertEquals(2.0, context.pop());
    }


    @Test
    void testMultiplyCommand() throws CommandException {
        context.push(2.0);
        context.push(3.0);
        factory.getCommand("*").execute(context, new String[]{});
        assertEquals(6.0, context.pop());
    }
    @Test
    void testThrowsMultiplyCommand() throws CommandException {
        context.push(2.0);
        assertThrows(CommandException.class, () -> factory.getCommand("*").execute(context, new String[]{}));
        assertEquals(2.0, context.pop());
    }

    @Test
    void testDivideCommand() throws CommandException {
        context.push(2.0);
        context.push(6.0);
        factory.getCommand("/").execute(context, new String[]{});
        assertEquals(3.0, context.pop());
    }

    @Test
    void testDivisionByZero() {
        context.push(0.0);
        context.push(5.0);
        assertThrows(CommandException.class,
                () -> factory.getCommand("/").execute(context, new String[]{}));
    }

    @Test
    void testSqrtCommand() throws CommandException {
        context.push(9.0);
        factory.getCommand("SQRT").execute(context, new String[]{});
        assertEquals(3.0, context.pop());
    }

    @Test
    void testSqrtNegative() {
        context.push(-9.0);
        assertThrows(CommandException.class,
                () -> factory.getCommand("SQRT").execute(context, new String[]{}));
    }

    @Test
    void testPushCommand() throws CommandException {
        factory.getCommand("PUSH").execute(context, new String[]{"5.0"});
        assertEquals(5.0, context.pop());
    }

    @Test
    void testPushVariable() throws CommandException {
        context.define("x", 10.0);
        factory.getCommand("PUSH").execute(context, new String[]{"x"});
        assertEquals(10.0, context.pop());
    }

    @Test
    void testPopCommand() throws CommandException {
        context.push(5.0);
        factory.getCommand("POP").execute(context, new String[]{});
        assertEquals(0, context.stackSize());
    }

    @Test
    void testDefineCommand() throws CommandException {
        factory.getCommand("DEFINE").execute(context, new String[]{"a", "5.0"});
        assertEquals(5.0, context.getDefine("a"));
    }


    @Test
    void testRemoveCommandSingle() throws CommandException {
        context.push(1.0);
        context.push(2.0);
        context.push(3.0);
        factory.getCommand("REMOVE").execute(context, new String[]{"1"});
        assertEquals(3.0, context.pop());
        assertEquals(1.0, context.pop());
    }

    @Test
    void testRemoveCommandRange() throws CommandException {
        context.push(1.0);
        context.push(2.0);
        context.push(3.0);
        context.push(4.0);
        factory.getCommand("REMOVE").execute(context, new String[]{"1", "3"});
        assertEquals(4.0, context.pop());
        assertEquals(1.0, context.pop());
    }

    @Test
    void testThrowsRemoveCommand()  {
        context.push(1.0);
        context.push(2.0);
        context.push(3.0);
        assertThrows(CommandException.class,
                () -> factory.getCommand("REMOVE").execute(context, new String[]{"1", "0"}));
        assertThrows(CommandException.class,
                () -> factory.getCommand("REMOVE").execute(context, new String[]{"1", "6"}));

        assertThrows(CommandException.class,
                () -> factory.getCommand("REMOVE").execute(context, new String[]{"-1", "0"}));

    }

    @Test
    void testInvalidCommand() {
        assertThrows(CommandException.class,
                () -> factory.getCommand("INVALID").execute(context, new String[]{}));
    }

    @Test
    void testEmptyStackOperations() {
        assertThrows(CommandException.class,
                () -> factory.getCommand("+").execute(context, new String[]{}));
    }
}