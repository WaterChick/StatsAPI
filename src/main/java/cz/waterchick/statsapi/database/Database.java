package cz.waterchick.statsapi.database;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

public class Database {

    private HikariDataSource hikari;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    private final JavaPlugin plugin;

    private static final String TABLE_PREFIX = "statsapi_";

    public Database(String host, String port, String database, String username, String password, JavaPlugin plugin) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.plugin = plugin;

        connect();
    }

    private void connect() {
        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(25);
        hikari.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.addDataSourceProperty("user", this.username);
        hikari.addDataSourceProperty("password", this.password);
    }

    public void createTable(String name) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_PREFIX + name +
                "(`uuid` CHAR(36), `value` INT(10) DEFAULT 0 NOT NULL, PRIMARY KEY (`uuid`));";
        try (Connection connection = hikari.getConnection();
             PreparedStatement p = connection.prepareStatement(sql)) {
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OptionalInt getValue(String name, String uuid) {
        String sql = "SELECT value FROM " + TABLE_PREFIX + name + " WHERE uuid = ?;";
        try (Connection connection = hikari.getConnection();
             PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, uuid);
            ResultSet resultSet = p.executeQuery();
            if (resultSet.next()) {
                return OptionalInt.of(resultSet.getInt("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return OptionalInt.empty();
    }

    public void setValue(String name, String uuid, Integer value) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "REPLACE INTO " + TABLE_PREFIX + name + " (uuid, value) VALUES (?, ?);";
            try (Connection connection = hikari.getConnection();
                 PreparedStatement p = connection.prepareStatement(sql)) {
                p.setString(1, uuid);
                p.setInt(2, value);
                p.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public List<String> getTableStatistics() {
        List<String> tableList = new ArrayList<>();
        String sql = "SHOW TABLES;";
        try (Connection connection = hikari.getConnection();
             PreparedStatement p = connection.prepareStatement(sql)) {
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString(1);
                if (tableName.startsWith(TABLE_PREFIX)) {
                    tableList.add(tableName.replace(TABLE_PREFIX, ""));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableList;
    }

    public void close() {
        if (hikari != null && !hikari.isClosed()) {
            hikari.close();
        }
    }
    public Connection getConnection() throws SQLException{
        return hikari.getConnection();
    }
}

