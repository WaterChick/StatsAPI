package cz.waterchick.statsapi;

import cz.waterchick.statsapi.database.DatabaseCredential;
import cz.waterchick.statsapi.managers.ApiManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;


public final class StatsAPI extends JavaPlugin {



    private static final Logger LOGGER = Logger.getLogger(StatsAPI.class.getName());
    private static ApiManager apiManager;
    private static StatsAPI plugin;

    public static ApiManager getAPI() {
        return apiManager;
    }

    public static StatsAPI getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        loadFiles();
        loadCredentials();
        if (isPapiEnabled()) {
            new PAPI().register();
            getLogger().info("Hooked to PlaceholderAPI!");
        }
        plugin = this;
        apiManager = new ApiManager(this); // Inicializace zde

    }

    @Override
    public void onDisable() {
        if (apiManager != null) {
            apiManager.getDatabase().close();
        }
    }

    private void loadFiles(){
        saveDefaultConfig();
        saveConfig();
        reloadConfig();
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
