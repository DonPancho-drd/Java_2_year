package calculator;

import calculator.Command;
import calculator.exceptions.CommandException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandFactory {
    private static final Logger logger = Logger.getLogger(CommandFactory.class.getName());
    private final Properties commands = new Properties();

    public CommandFactory() throws CommandException {
        logger.log(Level.INFO, "Initializing CommandFactory");
        try (InputStream input = getClass().getResourceAsStream("commands.properties")) {
            if (input == null) {
                String errorMsg = "Commands configuration file not found";
                logger.log(Level.SEVERE, errorMsg);
                throw new CommandException(errorMsg);
            }
            commands.load(input);
            logger.log(Level.INFO, "Successfully loaded {0} commands", commands.size());
        } catch (IOException e) {
            String errorMsg = "Failed to load commands configuration";
            logger.log(Level.SEVERE, errorMsg, e);
            throw new CommandException(errorMsg, e);
        }
    }

    public Command getCommand(String name) throws CommandException {
        logger.log(Level.FINE, "Requesting command: {0}", name);

        String className = commands.getProperty(name);
        if (className == null) {
            String errorMsg = "Unknown command: " + name;
            logger.log(Level.WARNING, errorMsg);
            throw new CommandException(errorMsg);
        }

        try {
            logger.log(Level.FINER, "Instantiating command class: {0}", className);
            Class<?> commandClass = Class.forName(className);
            Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
            logger.log(Level.FINE, "Successfully created command instance: {0}", name);
            return command;
        } catch (ReflectiveOperationException e) {
            String errorMsg = "Failed to instantiate command: " + name;
            logger.log(Level.SEVERE, errorMsg, e);
            throw new CommandException(errorMsg, e);
        }
    }
}