package de.groodian.hyperiorcloud.logging;

public abstract class LogHandler {

    protected LogEntryFormatter logEntryFormatter;
    protected LogLevel logLevel;

    public LogHandler(LogEntryFormatter logEntryFormatter, LogLevel logLevel) {
        this.logEntryFormatter = logEntryFormatter;
        this.logLevel = logLevel;
    }

    public abstract void handle(LogEntry logEntry);

    public abstract void close();

    public LogEntryFormatter getLogEntryFormatter() {
        return logEntryFormatter;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
