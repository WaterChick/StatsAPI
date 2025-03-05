package cz.waterchick.statsapi.managers;

import cz.waterchick.statsapi.database.Database;
import cz.waterchick.statsapi.database.DatabaseCredential;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiManager {

    private static final Logger LOGGER = Logger.getLogger(ApiManager.class.getName());

    private final StatisticManager statisticManager;
    private final Database database;

    public ApiManager() {
        this.database = new Database(
                DatabaseCredential.HOST.getValue(),
                DatabaseCredential.PORT.getValue(),
                DatabaseCredential.DATABASE.getValue(),
                DatabaseCredential.USERNAME.getValue(),
                DatabaseCredential.PASSWORD.getValue()
        );
        connectWithRetry();
        this.statisticManager = new StatisticManager(database);
    }

    private void connectWithRetry() {
        int retryAttempts = 3;
        int retryDelay = 5000; // 5 seconds
        for (int i = 0; i < retryAttempts; i++) {
            try {
                Thread.sleep(3000); // Initial delay
                database.connect();
                //Attempt to get a connection to test connection.
                database.getConnection().close();
                LOGGER.info("Database connection successful.");
                return; // Connection successful, exit the loop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted state
                LOGGER.log(Level.SEVERE, "Thread interrupted during database connection attempt.", e);
                return; // Exit the loop
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database connection failed (attempt " + (i + 1) + ").", e);
                if (i < retryAttempts - 1) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        LOGGER.log(Level.SEVERE, "Retry delay interrupted.", ex);
                        return;
                    }
                }
            }
        }
        LOGGER.severe("Failed to establish database connection after " + retryAttempts + " attempts.");
    }

    public StatisticManager getStatisticManager() {
        return statisticManager;
    }

    public Database getDatabase() {
        return database;
    }

    public boolean testConnection() {
        try {
            database.getConnection().close();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection test failed.", e);
            return false;
        }
    }
}