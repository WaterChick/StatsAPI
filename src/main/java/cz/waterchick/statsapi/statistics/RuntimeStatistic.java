package cz.waterchick.statsapi.statistics;

import java.util.*;

public class RuntimeStatistic extends AbstractStatistic {

    private final Map<String, Integer> values = new HashMap<>();

    public RuntimeStatistic(String name) {
        super(name);
    }

    @Override
    public Integer getValue(String uuid) {
        return values.getOrDefault(uuid, 0);
    }

    @Override
    public void setValue(String uuid, Integer value) {
        values.put(uuid, value);
    }

    @Override
    public void increment(String uuid) {
        Integer currentValue = getValue(uuid);
        setValue(uuid, currentValue + 1);
    }

    @Override
    public void decrease(String uuid) {
        Integer currentValue = getValue(uuid);
        setValue(uuid, currentValue - 1);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public Set<Map.Entry<String, Integer>> getAll() {
        return values.entrySet();
    }
}
