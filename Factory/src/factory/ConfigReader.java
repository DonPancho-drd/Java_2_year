package factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private int storageBodySize = 100;
    private int storageMotorSize = 100;
    private int storageAccessorySize = 100;
    private int storageCarSize = 100;
    private int accessorySuppliers = 10;
    private int workers = 10;
    private int dealers = 10;
    private boolean log = true;

    public ConfigReader(String configFilePath) throws IOException, IllegalArgumentException{

        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            if (inputStream == null) {
                throw new IOException("Configuration file not found: " + configFilePath);
            }
            properties.load(inputStream);
        }

        storageBodySize = parsePositiveInt(properties, "StorageBodySize", storageBodySize);
        storageMotorSize = parsePositiveInt(properties, "StorageMotorSize", storageMotorSize);
        storageAccessorySize = parsePositiveInt(properties, "StorageAccessorySize", storageAccessorySize);
        storageCarSize = parsePositiveInt(properties, "StorageCarSize", storageCarSize);
        accessorySuppliers = parsePositiveInt(properties, "AccessorySuppliers", accessorySuppliers);
        workers = parsePositiveInt(properties, "Workers", workers);
        dealers = parsePositiveInt(properties, "Dealers", dealers);
        log = Boolean.parseBoolean(properties.getProperty("Log", String.valueOf(log)));
    }

    private int parsePositiveInt(Properties properties, String key, int defaultValue) {
        String value = properties.getProperty(key, String.valueOf(defaultValue));
        try {
            int result = Integer.parseInt(value);
            if (result <= 0) {
                throw new IllegalArgumentException("Parameter " + key + " must be positive, got: " + result);
            }
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for " + key + ": " + value, e);
        }
    }


    public int getStorageBodySize() {
        return storageBodySize;
    }
    public int getStorageEngineSize() {
        return storageMotorSize;
    }
    public int getStorageAccessorySize() {
        return storageAccessorySize;
    }
    public int getStorageCarSize() {
        return storageCarSize;
    }
    public int getAccessorySuppliers() {
        return accessorySuppliers;
    }
    public int getWorkers() {
        return workers;
    }
    public int getDealers() {
        return dealers;
    }
    public boolean isLog() {
        return log;
    }

}