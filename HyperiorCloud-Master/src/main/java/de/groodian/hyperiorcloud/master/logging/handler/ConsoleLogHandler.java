package de.groodian.hyperiorcloud.master.logging.handler;

import de.groodian.hyperiorcloud.master.console.Console;
import de.groodian.hyperiorcloud.master.logging.LogEntry;
import de.groodian.hyperiorcloud.master.logging.LogEntryFormatter;
import de.groodian.hyperiorcloud.master.logging.LogHandler;
import de.groodian.hyperiorcloud.master.logging.LogLevel;

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
