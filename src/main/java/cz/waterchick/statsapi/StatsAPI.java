package cz.waterchick.statsapi;

import cz.waterchick.statsapi.database.DatabaseCredential;
import cz.waterchick.statsapi.listener.PlayerListener;
import cz.waterchick.statsapi.managers.ApiManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class StatsAPI extends JavaPlugin {

    private static ApiManager apiManager;


    public static ApiManager getAPI() {
        return apiManager;
    }

    @Override
    public void onEnable() {
        registerListeners();
        loadFiles();
        loadCredentials();

        apiManager = new ApiManager();

        if(isPapiEnabled()){
            new PAPI().register();
            getLogger().info("Hooked to PlaceholderAPI!");
        }

    }

    @Override
    public void onDisable(){
        apiManager.getStatisticManager().saveStatistics();
        apiManager.getDatabase().close();
    }

    private void loadFiles(){
        saveDefaultConfig();
        saveConfig();
        reloadConfig();
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void loadCredentials(){
        DatabaseCredential.HOST.setValue(getConfig().getString("mysql.host"));
        DatabaseCredential.PORT.setValue(getConfig().getString("mysql.port"));
        DatabaseCredential.DATABASE.setValue(getConfig().getString("mysql.database"));
        DatabaseCredential.USERNAME.setValue(getConfig().getString("mysql.username"));
        DatabaseCredential.PASSWORD.setValue(getConfig().getString("mysql.password"));
    }

    private boolean isPapiEnabled(){
        return (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);
    }

}
