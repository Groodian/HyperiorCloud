package de.groodian.hyperiorcloud.master.logging.formatter;

import de.groodian.hyperiorcloud.master.logging.LogEntry;
import de.groodian.hyperiorcloud.master.logging.ILogEntryFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsoleLogEntryFormatter implements ILogEntryFormatter {

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    @Override
    public String format(LogEntry logEntry) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("[")
                .append(dateFormat.format(logEntry.getTime()))
                .append("] [")
                .append(logEntry.getLogLevel().toString())
                .append("] > ")
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

        return stringBuilder.toString();
    }

}
