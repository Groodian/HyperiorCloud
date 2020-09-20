package de.groodian.hyperiorcloud.master.logging.formatter;

import de.groodian.hyperiorcloud.master.console.ConsoleColor;
import de.groodian.hyperiorcloud.master.logging.LogEntry;
import de.groodian.hyperiorcloud.master.logging.LogEntryFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsoleLogEntryFormatter implements LogEntryFormatter {

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    @Override
    public String format(LogEntry logEntry) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("&8[&7")
                .append(dateFormat.format(logEntry.getTime()))
                .append("&8] [")
                .append(logEntry.getLogLevel().getColor())
                .append(logEntry.getLogLevel().toString())
                .append("&8]")
                .append(" &7> &f")
                .append(logEntry.getMessage());

        if (logEntry.getThrowable() != null && logEntry.getThrowable().getStackTrace()[0] != null) {
            stringBuilder
                    .append(" [Cause: ")
                    .append(logEntry.getThrowable())
                    .append(" at ")
                    .append(logEntry.getThrowable().getStackTrace()[0])
                    .append("]");
        }

        stringBuilder.append(System.lineSeparator());

        return ConsoleColor.translateColorCodes(stringBuilder.toString());
    }

}
