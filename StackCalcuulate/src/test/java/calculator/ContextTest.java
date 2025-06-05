package test.java.calculator;

import calculator.Context;
import calculator.exceptions.CommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {
    private Context context;

    @BeforeEach
    public void setUp() {
        context = new Context();
    }

    @Test
    void pushAndPopShouldWorkCorrectly() throws CommandException {
        context.push(10.5);
        assertEquals(10.5, context.pop(), "Должен возвращать последнее добавленное значение");
    }

    @Test
    void popEmptyStackShouldThrowException() {
        assertThrows(CommandException.class,
                () -> context.pop(),
                "Должен бросать исключение при попытке взять из пустого стека");
    }

    @Test
    void peekShouldReturnLastElementWithoutRemoving() throws CommandException {
        context.push(20.0);
        assertEquals(20.0, context.peek(), "peek должен возвращать верхний элемент");
        assertEquals(1, context.stackSize(), "peek не должен удалять элемент");
    }

    @Test
    void peekEmptyStackShouldThrowException() {
        assertThrows(CommandException.class,
                () -> context.peek(),
                "Должен бросать исключение при peek пустого стека");
    }

    @Test
    void defineAndGetShouldWorkCorrectly() throws CommandException {
        context.define("x", 15.0);
        assertEquals(15.0, context.getDefine("x"), "Должен возвращать определенное значение");
    }

    @Test
    void getUndefinedVariableShouldThrowException() {
        assertThrows(CommandException.class,
                () -> context.getDefine("y"),
                "Должен бросать исключение при запросе неопределенной переменной");
    }

}