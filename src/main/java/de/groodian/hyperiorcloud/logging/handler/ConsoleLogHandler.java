package de.groodian.hyperiorcloud.logging.handler;

import de.groodian.hyperiorcloud.console.Console;
import de.groodian.hyperiorcloud.logging.LogEntry;
import de.groodian.hyperiorcloud.logging.LogEntryFormatter;
import de.groodian.hyperiorcloud.logging.LogHandler;
import de.groodian.hyperiorcloud.logging.LogLevel;

public class ConsoleLogHandler extends LogHandler {

    private Console console;

    public ConsoleLogHandler(LogEntryFormatter logEntryFormatter, LogLevel logLevel, Console console) {
        super(logEntryFormatter, logLevel);
        this.console = console;
    }

    @Override
    public void handle(LogEntry logEntry) {
        console.printLine(logEntryFormatter.format(logEntry));
    }

    @Override
    public void close() {
    }

}
