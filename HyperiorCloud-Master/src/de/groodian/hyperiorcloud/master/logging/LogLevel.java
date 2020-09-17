package de.groodian.hyperiorcloud.master.logging;

public enum LogLevel {

    FATAL(0),
    ERROR(1),
    WARNING(2),
    IMPORTANT(3),
    COMMAND(4),
    INFO(5),
    DEBUG(6),
    ALL(Integer.MAX_VALUE);

    private int level;

    private LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
