package ru.akirakozov.sd.refactoring.database;

public class DataBaseException extends RuntimeException {
    public DataBaseException(final String message) {
        super(message);
    }

    public DataBaseException(final String message, final Exception e) {
        super(message, e);
    }
}
