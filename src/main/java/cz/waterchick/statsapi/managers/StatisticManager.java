package cz.waterchick.statsapi.managers;

import cz.waterchick.statsapi.database.Database;
import cz.waterchick.statsapi.statistics.AbstractStatistic;
import cz.waterchick.statsapi.statistics.DatabaseStatistic;
import cz.waterchick.statsapi.statistics.RuntimeStatistic;

import java.sql.SQLException;
import java.util.*;

public class StatisticManager {


    private final List<AbstractStatistic> statistics = new ArrayList<>();

    private final Database database;

    public StatisticManager(Database database) {
        this.database = database;
    }

    public void createRuntimeStatistic(String name) {
        if(doesStatisticExist(name)) return;
        RuntimeStatistic statistic = new RuntimeStatistic(name);
        statistics.add(statistic);
    }

    public void createDatabaseStatistic(String name) {
        if(doesStatisticExist(name)) return;
        DatabaseStatistic statistic = new DatabaseStatistic(name,database);
        statistics.add(statistic);
    }

    public boolean doesStatisticExist(String name){
        return getStatistic(name) != null;
    }

    public AbstractStatistic getStatistic(String name){
        AbstractStatistic returnStatistic = null;
        for(AbstractStatistic statistic : statistics){
            if(!statistic.getName().equals(name)){
                continue;
            }
            returnStatistic = statistic;
        }
        return returnStatistic;
    }


    public void savePlayerStatistics(String uuid) {
        for (AbstractStatistic statistic : statistics) {
            if (statistic instanceof DatabaseStatistic) {
                Integer playerValue = statistic.getValue(uuid);
                database.setValue(statistic.getName(), uuid, playerValue);
            }
        }
    }

    public void loadPlayerStatistics(String uuid) throws SQLException {
        for (String statisticName : database.getTableStatistics()) {
            if (!doesStatisticExist(statisticName)) {
                createDatabaseStatistic(statisticName);
            }
            DatabaseStatistic statistic = (DatabaseStatistic) getStatistic(statisticName);
            OptionalInt value = database.getValue(statisticName, uuid);
            value.ifPresent(v -> statistic.setValue(uuid, v));
        }
    }
}


