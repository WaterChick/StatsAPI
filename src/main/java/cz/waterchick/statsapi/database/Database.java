package cz.waterchick.statsapi.database;

import com.zaxxer.hikari.HikariDataSource;

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

    private static final String TABLE_PREFIX = "statsapi_";

    public Database(String host, String port, String database, String username, String password){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect(){

        hikari = new HikariDataSource();
        //Setting Hikari properties
        hikari.setMaximumPoolSize(25);
        hikari.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.addDataSourceProperty("serverName", this.host);
        hikari.addDataSourceProperty("port", this.port);
        hikari.addDataSourceProperty("databaseName", this.database);
        hikari.addDataSourceProperty("user", this.username);
        hikari.addDataSourceProperty("password", this.password);
    }

    public void createTable(String name){
        Connection connection = null;
        //MySQL query: https://www3.ntu.edu.sg/home/ehchua/programming/sql/MySQL_Beginner.html

        PreparedStatement p = null;

        try {
            //Initialise hikari connection, by getting the hikari connect if established
            connection = hikari.getConnection();
            //Preparing statement - INSERT INTO...
            p = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "+TABLE_PREFIX+name+"(`uuid` CHAR(36), `value` INT(10) DEFAULT 0 NOT NULL, PRIMARY KEY (`uuid`));");
            //Setting parameters in MySQL query: i.e the question marks(?), where the first one has the index of 1.
            //Executes the statement
            p.execute();
        } catch (SQLException e) {
            //Print out any exception while trying to prepare statement
            e.printStackTrace();
        } finally {
            //After catching the statement, close connection if connection is established
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // If connection is established, close connection after query
            if(p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public OptionalInt getValue(String name, String uuid) {
        Connection connection = null;
        //MySQL query: https://www3.ntu.edu.sg/home/ehchua/programming/sql/MySQL_Beginner.html

        PreparedStatement p = null;

        try {
            //Initialise hikari connection, by getting the hikari connect if established
            connection = hikari.getConnection();
            //Preparing statement - INSERT INTO...
            p = connection.prepareStatement("SELECT value FROM "+TABLE_PREFIX+name+" WHERE uuid = ?;");
            //Setting parameters in MySQL query: i.e the question marks(?), where the first one has the index of 1.
            //Executes the statement
            p.setString(1, uuid);
            ResultSet resultSet = p.executeQuery();
            if (resultSet.next()) {
                return OptionalInt.of(resultSet.getInt("value"));
            }
        } catch (SQLException e) {
            //Print out any exception while trying to prepare statement
            e.printStackTrace();
        } finally {
            //After catching the statement, close connection if connection is established
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // If connection is established, close connection after query
            if(p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return OptionalInt.empty();
    }

    public void setValue(String name, String uuid, Integer value){
        Connection connection = null;
        //MySQL query: https://www3.ntu.edu.sg/home/ehchua/programming/sql/MySQL_Beginner.html

        PreparedStatement p = null;

        try {
            //Initialise hikari connection, by getting the hikari connect if established
            connection = hikari.getConnection();
            //Preparing statement - INSERT INTO...
            p = connection.prepareStatement("REPLACE "+TABLE_PREFIX+name+"(uuid, value) VALUES(?, ?);");
            //Setting parameters in MySQL query: i.e the question marks(?), where the first one has the index of 1.
            p.setString(1, uuid);
            p.setInt(2, value);
            //Executes the statement
            p.execute();
        } catch (SQLException e) {
            //Print out any exception while trying to prepare statement
            e.printStackTrace();
        } finally {
            //After catching the statement, close connection if connection is established
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // If connection is established, close connection after query
            if(p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close ()
    {
        if(hikari == null || hikari.isClosed()){
            return;
        }
        hikari.close();
    }

    public List<String> getTableStatistics(){
        List<String> tableList = new ArrayList<>();
        Connection connection = null;
        //MySQL query: https://www3.ntu.edu.sg/home/ehchua/programming/sql/MySQL_Beginner.html

        PreparedStatement p = null;

        try {
            //Initialise hikari connection, by getting the hikari connect if established
            connection = hikari.getConnection();
            //Preparing statement - INSERT INTO...
            //Setting parameters in MySQL query: i.e the question marks(?), where the first one has the index of 1.
            //Executes the statement

            p = connection.prepareStatement("Show tables;");

            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                final String[] stringArray = rs.getString(1).split("_");
                if(stringArray.length != 2 || !stringArray[0].equals(TABLE_PREFIX.replace("_", ""))){
                    continue;
                }
                tableList.add(stringArray[1]);
            }
        } catch (SQLException e) {
            //Print out any exception while trying to prepare statement
            e.printStackTrace();
        } finally {
            //After catching the statement, close connection if connection is established
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // If connection is established, close connection after query
            if(p != null) {
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tableList;
    }



}
