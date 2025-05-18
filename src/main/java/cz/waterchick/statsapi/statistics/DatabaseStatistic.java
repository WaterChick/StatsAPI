package cz.waterchick.statsapi.statistics;

import cz.waterchick.statsapi.database.Database;

import java.util.*;

public class DatabaseStatistic extends AbstractStatistic {

    private final Database database;

    public DatabaseStatistic(String name, Database database) {
        super(name);
        this.database = database;
    }

    @Override
    public Integer getValue(String uuid) {
        OptionalInt value = database.getValue(getName(), uuid);
        return value.orElse(0);
    }

    @Override
    public void setValue(String uuid, Integer value) {
        database.setValue(getName(), uuid, value);
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
        database.dropTable(getName());
        database.createTable(getName());
    }

    @Override
    public Set<Map.Entry<String, Integer>> getAll() {
        return database.getAll(getName()).entrySet();
    }
}