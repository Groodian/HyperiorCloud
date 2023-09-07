package de.groodian.hyperiorcloud.logging;

import de.groodian.hyperiorcloud.console.ConsoleColor;

public enum LogLevel {

    FATAL(0, ConsoleColor.DARK_RED),
    ERROR(1, ConsoleColor.RED),
    WARNING(2, ConsoleColor.GOLD),
    IMPORTANT(3, ConsoleColor.YELLOW),
    INFO(4, ConsoleColor.DEFAULT),
    COMMAND(5, ConsoleColor.DEFAULT),
    DEBUG(6, ConsoleColor.DEFAULT),
    ALL(Integer.MAX_VALUE, ConsoleColor.DEFAULT);

    private final int level;
    private final ConsoleColor color;

    LogLevel(int level, ConsoleColor color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public ConsoleColor getColor() {
        return color;
    }

}
