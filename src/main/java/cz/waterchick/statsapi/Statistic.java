package cz.waterchick.statsapi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Statistic {

    private final String name;
    private final boolean save;

    private final Map<UUID, Integer> playerValue = new HashMap<>();


    public Statistic(String name, boolean save){
        this.name = name;
        this.save = save;
    }


    public String getName() {
        return name;
    }

    public Integer getValue(String uuid) {
        return playerValue.computeIfAbsent(UUID.fromString(uuid), k -> 0);
    }

    public Integer setValue(String uuid, Integer value) {
        return playerValue.put(UUID.fromString(uuid), value);
    }


    public void increment(String uuid){
        int newValue = getValue(uuid)+1;
        playerValue.put(UUID.fromString(uuid), newValue);
    }

    public void decrease(String uuid){
        int newValue = getValue(uuid)-1;
        playerValue.put(UUID.fromString(uuid), newValue);
    }

    public Map<UUID, Integer> getPlayerValue() {
        return playerValue;
    }

    public boolean isSave() {
        return save;
    }
}


