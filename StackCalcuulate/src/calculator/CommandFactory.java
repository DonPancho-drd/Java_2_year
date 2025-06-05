package calculator;

import calculator.exceptions.CommandException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommandFactory {
    private final Properties commands = new Properties();


    public CommandFactory() throws CommandException {
        this("commands.properties");
    }

    public CommandFactory(String configFile) throws CommandException {
        try (InputStream input = getClass().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new CommandException("Configuration file not found: " + configFile);
            }
            commands.load(input);
        } catch (IOException e) {
            throw new CommandException("Failed to load commands configuration", e);
        }
    }


    public Command getCommand(String name) throws CommandException {
        String className = commands.getProperty(name);
        if (className == null) {
            throw new CommandException("Unknown command: " + name);
        }

        try {
            return (Command) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new CommandException("Failed to create command: " + name, e);
        }
    }
}