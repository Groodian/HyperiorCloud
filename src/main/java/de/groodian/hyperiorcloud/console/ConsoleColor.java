package de.groodian.hyperiorcloud.console;

import org.jline.utils.AttributedStyle;

public enum ConsoleColor {

    DEFAULT(AttributedStyle.DEFAULT),
    BLACK(AttributedStyle.DEFAULT.foreground(0, 0, 0)),
    DARK_BLUE(AttributedStyle.DEFAULT.foreground(0, 0, 139)),
    DARK_GREEN(AttributedStyle.DEFAULT.foreground(1, 50, 32)),
    DARK_AQUA(AttributedStyle.DEFAULT.foreground(5, 105, 107)),
    DARK_RED(AttributedStyle.DEFAULT.foreground(139, 0, 0)),
    DARK_PURPLE(AttributedStyle.DEFAULT.foreground(139, 0, 0)),
    GOLD(AttributedStyle.DEFAULT.foreground(255, 215, 0)),
    GRAY(AttributedStyle.DEFAULT.foreground(138, 138, 138)),
    DARK_GRAY(AttributedStyle.DEFAULT.foreground(90, 90, 90)),
    BLUE(AttributedStyle.DEFAULT.foreground(0, 0, 255)),
    GREEN(AttributedStyle.DEFAULT.foreground(0, 128, 0)),
    AQUA(AttributedStyle.DEFAULT.foreground(0, 255, 255)),
    RED(AttributedStyle.DEFAULT.foreground(255, 0, 0)),
    LIGHT_PURPLE(AttributedStyle.DEFAULT.foreground(203, 195, 227)),
    YELLOW(AttributedStyle.DEFAULT.foreground(255, 255, 0)),
    WHITE(AttributedStyle.DEFAULT.foreground(255, 255, 255));

    private final AttributedStyle style;

    ConsoleColor(AttributedStyle style) {
        this.style = style;
    }

    public AttributedStyle getStyle() {
        return style;
    }

}
