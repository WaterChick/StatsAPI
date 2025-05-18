package cz.waterchick.statsapi.statistics;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractStatistic {

    private final String name;

    public AbstractStatistic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Integer getValue(String uuid);

    public abstract void setValue(String uuid, Integer value);

    public abstract void increment(String uuid);

    public abstract void decrease(String uuid);

    public abstract void clear();

    public abstract Set<Map.Entry<String, Integer>> getAll();
}
