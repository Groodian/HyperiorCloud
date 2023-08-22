package de.groodian.hyperiorcloud.logging;

public class LogEntry {

    private LogLevel logLevel;
    private long time;
    private String message;
    private Throwable throwable;
    private Thread thread;
    private Class<?> clazz;

    public LogEntry(LogLevel logLevel, long time, String message, Throwable throwable, Thread thread, Class<?> clazz) {
        this.logLevel = logLevel;
        this.time = time;
        this.message = message;
        this.throwable = throwable;
        this.thread = thread;
        this.clazz = clazz;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Thread getThread() {
        return thread;
    }

    public Class<?> getClazz() {
        return clazz;
    }

}
