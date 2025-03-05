package cz.waterchick.statsapi.managers;

import cz.waterchick.statsapi.Statistic;
import cz.waterchick.statsapi.StatsAPI;
import cz.waterchick.statsapi.database.Database;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.*;

public class StatisticManager {


    private final List<Statistic> statistics = new ArrayList<>();

    private final Database database;

    public StatisticManager(Database database) {
        this.database = database;
    }

    public void createStatistic(String name, boolean save) throws SQLException { // Add throws SQLException
        if(doesStatisticExist(name)) return;
        Statistic statistic = new Statistic(name, save, database);
        statistics.add(statistic);
        if(save) StatsAPI.getAPI().getDatabase().createTable(name); // Now throws SQLException
    }

    public boolean doesStatisticExist(String name){
        return getStatistic(name) != null;
    }

    public Statistic getStatistic(String name){
        Statistic returnStatistic = null;
        for(Statistic statistic : statistics){
            if(!statistic.getName().equals(name)){
                continue;
            }
            returnStatistic = statistic;
        }
        return returnStatistic;
    }

    protected List<Statistic> getStatistics(){
        return Collections.unmodifiableList(statistics);
    }

    public void savePlayerStatistics(String uuid) {
        for (Statistic statistic : statistics) {
            if (statistic.isSave()) {
                Integer playerValue = statistic.getValue(uuid);
                database.setValue(statistic.getName(), uuid, playerValue);
            }
        }
    }

    public void loadPlayerStatistics(String uuid) throws SQLException {
        for (String statisticName : database.getTableStatistics()) {
            if (!doesStatisticExist(statisticName)) {
                createStatistic(statisticName, true);
            }
            Statistic statistic = getStatistic(statisticName);
            OptionalInt value = database.getValue(statisticName, uuid);
            value.ifPresent(v -> statistic.setValue(uuid, v));
        }
    }
}


