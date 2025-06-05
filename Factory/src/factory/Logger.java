package factory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final boolean logSale;
    private final String logFileName;
    private BufferedWriter writer;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Logger(boolean logSale, String logFileName) throws IOException {
        this.logSale = logSale;
        this.logFileName = logFileName != null ? logFileName : "factory_sales.log";
        if (logSale) {
            new FileWriter(logFileName, false).close();
            try {
                writer = new BufferedWriter(new FileWriter(this.logFileName, true));
            } catch (IOException e) {
                System.err.println("Failed to open log file: " + this.logFileName);
                throw e;
            }
        }
    }

    public synchronized void logSale(int dealerNumber, Car car) {
        if (!logSale || writer == null) {
            return;
        }
        String logEntry = String.format("%s: Dealer %d: Auto %d (Body: %d, Motor: %d, Accessory: %d)",
                LocalDateTime.now().format(formatter),
                dealerNumber,
                car.getID(),
                car.getBody().getID(),
                car.getEngine().getID(),
                car.getAccessory().getID());
        try {
            writer.write(logEntry);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Failed to close log file: " + e.getMessage());
            } finally {
                writer = null;
            }
        }
    }
}