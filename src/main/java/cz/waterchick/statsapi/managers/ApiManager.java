package cz.waterchick.statsapi.managers;

import cz.waterchick.statsapi.StatsAPI;
import cz.waterchick.statsapi.database.Database;
import cz.waterchick.statsapi.database.DatabaseCredential;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiManager {

    private static final Logger LOGGER = Logger.getLogger(ApiManager.class.getName());

    private final StatisticManager statisticManager;
    private final Database database;

    public ApiManager(JavaPlugin plugin) {
        this.database = new Database(
                DatabaseCredential.HOST.getValue(),
                DatabaseCredential.PORT.getValue(),
                DatabaseCredential.DATABASE.getValue(),
                DatabaseCredential.USERNAME.getValue(),
                DatabaseCredential.PASSWORD.getValue(),
                plugin
        );
        this.statisticManager = new StatisticManager(database);
        connectWithRetry();
    }

    private void connectWithRetry() {
        LOGGER.info("Database connection attempt. Thread: " + Thread.currentThread().getName()); //Add this line
        int retryAttempts = 5;
        int retryDelay = 20; // 20 ticks (1 second)
        new BukkitRunnable() {
            int attempts = 0;

            @Override
            public void run() {
                try (Connection connection = database.getConnection()){
                    LOGGER.log(Level.INFO, "Database connection successful. Thread: " + Thread.currentThread().getName());
                    cancel(); // Stop the retry task
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Database connection failed (attempt " + (attempts + 1) + ").", e);
                    attempts++;
                    if (attempts >= retryAttempts) {
                        LOGGER.severe("Failed to connect to the database after " + retryAttempts + " attempts.");
                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(StatsAPI.getPlugin(), 0L, retryDelay);
    }

    public StatisticManager getStatisticManager() {
        return statisticManager;
    }

    public Database getDatabase() {
        return database;
    }

    public boolean testConnection() {
        try (Connection connection = database.getConnection()){
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection test failed.", e);
            return false;
        }
    }
}