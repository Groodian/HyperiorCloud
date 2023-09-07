package de.groodian.hyperiorcloud.logging.formatter;

import de.groodian.hyperiorcloud.console.ConsoleColor;
import de.groodian.hyperiorcloud.logging.LogEntry;
import de.groodian.hyperiorcloud.logging.LogEntryFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.jline.utils.AttributedStringBuilder;

public class ConsoleLogEntryFormatter implements LogEntryFormatter {

    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    @Override
    public String format(LogEntry logEntry) {
        AttributedStringBuilder attributedStringBuilder = new AttributedStringBuilder()
                .append("[", ConsoleColor.DARK_GRAY.getStyle())
                .append(dateFormat.format(logEntry.getTime()), ConsoleColor.GRAY.getStyle())
                .append("] [", ConsoleColor.DARK_GRAY.getStyle())
                .append(logEntry.getLogLevel().toString(), logEntry.getLogLevel().getColor().getStyle())
                .append("] ", ConsoleColor.DARK_GRAY.getStyle())
                .append("> ", ConsoleColor.GRAY.getStyle())
                .append(logEntry.getMessage(), ConsoleColor.WHITE.getStyle());

        if (logEntry.getThrowable() != null && logEntry.getThrowable().getStackTrace()[0] != null) {
            attributedStringBuilder
                    .append(" [Cause: ")
                    .append(logEntry.getThrowable().toString())
                    .append(" at ")
                    .append(logEntry.getThrowable().getStackTrace()[0].toString())
                    .append("]");
        }

        return attributedStringBuilder.toAnsi();
    }

}
