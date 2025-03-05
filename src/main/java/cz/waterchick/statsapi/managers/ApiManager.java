package cz.waterchick.statsapi.managers;

import cz.waterchick.statsapi.database.Database;
import cz.waterchick.statsapi.database.DatabaseCredential;

public class ApiManager {

    private final StatisticManager statisticManager;
    private final Database database;


    public ApiManager(){
        this.database = new Database(
                DatabaseCredential.HOST.getValue(),
                DatabaseCredential.PORT.getValue(),
                DatabaseCredential.DATABASE.getValue(),
                DatabaseCredential.USERNAME.getValue(),
                DatabaseCredential.PASSWORD.getValue()
        );
        this.statisticManager = new StatisticManager(database);
        database.connect();
    }

    public StatisticManager getStatisticManager() {
        return statisticManager;
    }

    public Database getDatabase() {
        return database;
    }
}
