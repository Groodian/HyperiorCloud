package de.groodian.hyperiorcloud.master.logging.formatter;

import de.groodian.hyperiorcloud.master.logging.LogEntry;
import de.groodian.hyperiorcloud.master.logging.LogEntryFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FileLogEntryFormatter implements LogEntryFormatter {

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    @Override
    public String format(LogEntry logEntry) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("[")
                .append(dateFormat.format(logEntry.getTime()))
                .append("] [")
                .append(logEntry.getLogLevel().toString())
                .append("]");

        if (logEntry.getThread() != null) {
            stringBuilder
                    .append(" [Thread: ")
                    .append(logEntry.getThread().getName())
                    .append("]");
        }

        if (logEntry.getClazz() != null) {
            stringBuilder
                    .append(" [Class: ")
                    .append(logEntry.getClazz().getName())
                    .append("]");
        }

        stringBuilder
                .append(" > ")
                .append(logEntry.getMessage());


        if (logEntry.getThrowable() != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            logEntry.getThrowable().printStackTrace(printWriter);
            stringBuilder
                    .append(" [StackTrace: ")
                    .append(stringWriter)
                    .append("]");
        }

        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }

}
