package cz.waterchick.statsapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "statsapi";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Water_Chick";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.1";
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){
        Statistic statistic = StatsAPI.getAPI().getStatisticManager().getStatistic(identifier);
        if(statistic == null){
            return null;
        }
        return statistic.getValue(player.getUniqueId().toString()).toString();
    }
}
