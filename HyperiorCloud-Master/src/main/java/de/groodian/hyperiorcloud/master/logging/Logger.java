package de.groodian.hyperiorcloud.master.logging;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    private List<LogHandler> logHandlers = new ArrayList<>();

    private LogLevel logLevel;

    public Logger(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void registerHandler(LogHandler logHandler) {
        if (logHandler != null) {
            logHandlers.add(logHandler);
        } else {
            error("Could not register log handler!", new IllegalArgumentException());
        }
    }

    public void unregisterHandler(LogHandler logHandler) {
        if (logHandlers.contains(logHandler)) {
            logHandlers.remove(logHandler);
        } else {
            error("Could not remove the log handler!", new IllegalArgumentException());
        }
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    public void fatal(String message, Throwable throwable) {
        log(LogLevel.FATAL, message, throwable);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    public void warning(String message, Throwable throwable) {
        log(LogLevel.WARNING, message, throwable);
    }

    public void important(String message) {
        log(LogLevel.IMPORTANT, message);
    }

    public void important(String message, Throwable throwable) {
        log(LogLevel.IMPORTANT, message, throwable);
    }

    public void command(String message) {
        log(LogLevel.COMMAND, message);
    }

    public void command(String message, Throwable throwable) {
        log(LogLevel.COMMAND, message, throwable);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void info(String message, Throwable throwable) {
        log(LogLevel.INFO, message, throwable);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void debug(String message, Throwable throwable) {
        log(LogLevel.DEBUG, message, throwable);
    }

    public void log(LogLevel logLevel, String message) {
        log(logLevel, message, null);
    }

    public void log(LogLevel logLevel, String message, Throwable throwable) {
        log(new LogEntry(logLevel, System.currentTimeMillis(), message, throwable, Thread.currentThread(), null));
    }

    public void log(LogEntry logEntry) {
        for (LogHandler logHandler : logHandlers) {
            if (logHandler.getLogLevel().getLevel() >= logEntry.getLogLevel().getLevel()) {
                logHandler.handle(logEntry);
            }
        }
    }

    public void close() {
        for (LogHandler logHandler : logHandlers) {
            logHandler.close();
        }
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public List<LogHandler> getHandlers() {
        return logHandlers;
    }

}
