package calculator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;
import java.io.File;

import calculator.exceptions.CommandException;
import java.util.logging.*;


public class Calculator {
    private static final Logger logger = Logger.getLogger(Calculator.class.getName());

    public static void main(String[] args) {
        try {
            logger.setLevel(Level.ALL);
            FileHandler fileHandler = new FileHandler("calculator.log");
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

            CommandFactory factory = new CommandFactory();
            Context context = new Context();
            Scanner scanner;

            if (args.length > 0) {
                scanner = new Scanner(new File(args[0]));
            } else {
                scanner = new Scanner(System.in);
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if(line.equals("stop")){
                    break;
                }
                String[] parts = line.split("\\s+");
                String commandName = parts[0];
                String[] commandArgs = new String[parts.length - 1];
                System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);

                try {
                    Command command = factory.getCommand(commandName);
                    command.execute(context, commandArgs);
                    logger.info("Executed command: " + commandName);
                } catch (CommandException e) {
                    logger.severe("Error executing command: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
        }
    }
}