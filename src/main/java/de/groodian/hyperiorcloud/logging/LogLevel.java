package de.groodian.hyperiorcloud.logging;

import de.groodian.hyperiorcloud.console.ConsoleColor;

public enum LogLevel {

    FATAL(0, ConsoleColor.DARK_RED.getAnsiCode()),
    ERROR(1, ConsoleColor.RED.getAnsiCode()),
    WARNING(2, ConsoleColor.GOLD.getAnsiCode()),
    IMPORTANT(3, ConsoleColor.YELLOW.getAnsiCode()),
    INFO(4, ConsoleColor.DEFAULT.getAnsiCode()),
    COMMAND(5, ConsoleColor.DEFAULT.getAnsiCode()),
    DEBUG(6, ConsoleColor.DEFAULT.getAnsiCode()),
    ALL(Integer.MAX_VALUE, ConsoleColor.DEFAULT.getAnsiCode());

    private int level;
    private String color;

    LogLevel(int level, String color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public String getColor() {
        return color;
    }

}
