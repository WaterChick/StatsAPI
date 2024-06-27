package cz.waterchick.statsapi.listener;

import cz.waterchick.statsapi.StatsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {


    @EventHandler
    public void LoadFromDB(PlayerLoginEvent event){
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        StatsAPI.getAPI().getStatisticManager().loadPlayerStatistics(uuid);
    }

    @EventHandler
    public void saveTODB(PlayerQuitEvent event){
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        StatsAPI.getAPI().getStatisticManager().savePlayerStatistics(uuid);
    }
}
