package de.groodian.hyperiorcloud.master.logging.handler;

import de.groodian.hyperiorcloud.master.logging.ILogEntryFormatter;
import de.groodian.hyperiorcloud.master.logging.LogEntry;
import de.groodian.hyperiorcloud.master.logging.LogHandler;
import de.groodian.hyperiorcloud.master.logging.LogLevel;

import java.io.PrintWriter;

public class ConsoleLogHandler extends LogHandler {

    private PrintWriter out;

    public ConsoleLogHandler(ILogEntryFormatter logEntryFormatter, LogLevel logLevel) {
        super(logEntryFormatter, logLevel);
        out = new PrintWriter(System.out);
    }

    @Override
    public void handle(LogEntry logEntry) {
        out.write(logEntryFormatter.format(logEntry));
        out.flush();
    }

    @Override
    public void close() {
        out.flush();
        out.close();
    }
}
