package cz.waterchick.statsapi.managers;

import cz.waterchick.statsapi.Statistic;
import cz.waterchick.statsapi.StatsAPI;
import cz.waterchick.statsapi.database.Database;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StatisticManager {


    private final List<Statistic> statistics = new ArrayList<>();

    public void createStatistic(String name, boolean save){
        if(doesStatisticExist(name)) return;
        Statistic statistic = new Statistic(name, save);
        statistics.add(statistic);
        if(save) StatsAPI.getAPI().getDatabase().createTable(name);
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

    public void savePlayerStatistics(String uuid){
        Database database = StatsAPI.getAPI().getDatabase();
        List<Statistic> statisticList = getStatistics();
        for(Statistic statistic : statisticList){
            if(!statistic.isSave()){
                continue;
            }
            String name = statistic.getName();
            Integer playerValue = statistic.getValue(uuid);
            database.setValue(name, uuid, playerValue);
        }
    }

    public void loadPlayerStatistics(String uuid){

        Database database = StatsAPI.getAPI().getDatabase();
        StatisticManager statisticManager = StatsAPI.getAPI().getStatisticManager();

        List<String> statisticList = database.getTableStatistics();

        for(String statisticName : statisticList){
            if(!statisticManager.doesStatisticExist(statisticName)){
                createStatistic(statisticName, true);
            }
            Statistic statistic = statisticManager.getStatistic(statisticName);
            String name = statistic.getName();
            OptionalInt value = database.getValue(name,uuid);
            if(value.isPresent()){
                statistic.setValue(uuid,value.getAsInt());
            }
        }
    }

    public void saveStatistics(){
        Database database = StatsAPI.getAPI().getDatabase();
        List<Statistic> statisticList = getStatistics();
        for(Statistic statistic : statisticList){
            if(!statistic.isSave()){
                continue;
            }
            String name = statistic.getName();
            for(Map.Entry<UUID, Integer> entry : statistic.getPlayerValue().entrySet()) {
                UUID uuid = entry.getKey();
                Integer value = entry.getValue();
                OptionalInt optionalValue = database.getValue(name, uuid.toString());
                if(optionalValue.isPresent() && optionalValue.getAsInt() > value){
                    continue;
                }
                database.setValue(name, uuid.toString(), value);
            }
        }
    }


}
