package de.groodian.hyperiorcloud.master.logging;

public abstract class LogHandler {

    protected ILogEntryFormatter logEntryFormatter;
    protected LogLevel logLevel;

    public LogHandler(ILogEntryFormatter logEntryFormatter, LogLevel logLevel) {
        this.logEntryFormatter = logEntryFormatter;
        this.logLevel = logLevel;
    }

    public abstract void handle(LogEntry logEntry);

    public abstract void close();

    public ILogEntryFormatter getLogEntryFormatter() {
        return logEntryFormatter;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
