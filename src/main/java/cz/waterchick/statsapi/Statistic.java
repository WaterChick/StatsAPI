package cz.waterchick.statsapi;

import cz.waterchick.statsapi.database.Database;

import java.util.OptionalInt;

public class Statistic {

    private final String name;
    private final boolean save;
    private final Database database;

    public Statistic(String name, boolean save, Database database) {
        this.name = name;
        this.save = save;
        this.database = database;
    }

    public String getName() {
        return name;
    }

    public Integer getValue(String uuid) {
        OptionalInt value = database.getValue(name, uuid);
        return value.orElse(0);
    }

    public void setValue(String uuid, Integer value) {
        database.setValue(name, uuid, value);
    }

    public void increment(String uuid) {
        Integer currentValue = getValue(uuid);
        setValue(uuid, currentValue + 1);
    }

    public void decrease(String uuid) {
        Integer currentValue = getValue(uuid);
        setValue(uuid, currentValue - 1);
    }

    public boolean isSave() {
        return save;
    }
}



