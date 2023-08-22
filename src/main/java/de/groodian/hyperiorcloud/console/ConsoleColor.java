package de.groodian.hyperiorcloud.console;

import de.groodian.hyperiorcloud.Master;
import org.fusesource.jansi.Ansi;

public enum ConsoleColor {

    DEFAULT('r', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT).boldOff().toString()),
    BLACK('0', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString()),
    DARK_BLUE('1', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString()),
    DARK_GREEN('2', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString()),
    DARK_AQUA('3', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString()),
    DARK_RED('4', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString()),
    DARK_PURPLE('5', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString()),
    GOLD('6', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).fg(Ansi.Color.YELLOW).boldOff().toString()),
    GRAY('7', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString()),
    DARK_GRAY('8', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString()),
    BLUE('9', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString()),
    GREEN('a', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString()),
    AQUA('b', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString()),
    RED('c', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString()),
    LIGHT_PURPLE('d', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString()),
    YELLOW('e', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString()),
    WHITE('f', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());


    private char code;
    private String ansiCode;

    ConsoleColor(char code, String ansiCode) {
        this.code = code;
        this.ansiCode = ansiCode;
    }

    public static String translateColorCodes(String message) {
        if (message != null) {
            for (ConsoleColor consoleColor : values()) {
                message = message.replace("&" + consoleColor.code, consoleColor.ansiCode);
            }
        } else {
            Master.getInstance().getLogger().error("Could not translate color codes!", new NullPointerException());
        }
        return message;
    }

    public static String removeColorCodes(String message) {
        if (message != null) {
            for (ConsoleColor consoleColor : values()) {
                message = message.replace("&" + consoleColor.code, "");
            }
        } else {
            Master.getInstance().getLogger().error("Could not remove color codes!", new NullPointerException());
        }
        return message;
    }

    public char getCode() {
        return code;
    }

    public String getAnsiCode() {
        return ansiCode;
    }

}
