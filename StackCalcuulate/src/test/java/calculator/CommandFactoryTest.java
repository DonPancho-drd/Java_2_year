package test.java.calculator;

import calculator.CommandFactory;
import calculator.exceptions.CommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandFactoryTest {
    private CommandFactory factory;

    @BeforeEach
    void setUp() throws CommandException {
        factory = new CommandFactory();
    }

    @Test
    void shouldCreateKnownCommands() throws CommandException {
        assertNotNull(factory.getCommand("+"), "Должен создавать команду сложения");
        assertNotNull(factory.getCommand("-"), "Должен создавать команду вычитания");
        assertNotNull(factory.getCommand("DEFINE"), "Должен создавать команду DEFINE");
    }

    @Test
    void shouldThrowExceptionForUnknownCommand() {
        assertThrows(CommandException.class, () -> factory.getCommand("UNKNOWN_COMMAND"),
                "Должен бросать исключение для неизвестных команд");
    }

    @Test
    void shouldThrowExceptionWhenConfigMissing() {
        assertThrows(CommandException.class, () -> new CommandFactory("missing.properties"),
                "Должен бросать исключение при отсутствии файла конфигурации");
    }

}