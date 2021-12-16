package ru.akirakozov.sd.refactoring.database;

public class DataBaseItem {
    private final String name;
    private final long cost;

    public DataBaseItem(final String name, final long cost) {
        this.name = name;
        this.cost = cost;
    }

    public final String getName() {
        return name;
    }

    public final long getCost() {
        return cost;
    }

}
