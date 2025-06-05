package chatapp.server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;


public class ServerLogger {
    private static FileWriter logFile;

    public static void init() {
            try {
                logFile = new FileWriter("server.log", false);
            } catch (IOException e) {
                System.err.println("Failed to open log file: " + e.getMessage());
            }
    }

    public static void log(String message) {
        if (logFile != null) {
            try {
                logFile.write("[" + LocalDateTime.now() + "] " + message + "\n");
                logFile.flush();
            } catch (IOException e) {
                System.err.println("Failed to write to log: " + e.getMessage());
            }
        }
        else{
            System.out.println("No file to log to.....");
        }
    }

    public static void close() {
        if (logFile != null) {
            try {
                logFile.close();
            } catch (IOException e) {
                System.err.println("Failed to close log file: " + e.getMessage());
            }
        }
    }
}