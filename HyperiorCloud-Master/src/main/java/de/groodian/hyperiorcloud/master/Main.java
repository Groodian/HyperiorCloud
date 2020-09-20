package de.groodian.hyperiorcloud.master;

import de.groodian.hyperiorcloud.master.console.Console;
import de.groodian.hyperiorcloud.master.logging.LogLevel;
import de.groodian.hyperiorcloud.master.logging.LogOutputStream;
import de.groodian.hyperiorcloud.master.logging.Logger;
import de.groodian.hyperiorcloud.master.logging.formatter.ConsoleLogEntryFormatter;
import de.groodian.hyperiorcloud.master.logging.formatter.FileLogEntryFormatter;
import de.groodian.hyperiorcloud.master.logging.handler.ConsoleLogHandler;
import de.groodian.hyperiorcloud.master.logging.handler.FileLogHandler;

import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {
        Logger logger = new Logger(LogLevel.ALL);
        Console console = new Console();

        logger.registerHandler(new FileLogHandler(new FileLogEntryFormatter(), LogLevel.ALL));
        logger.registerHandler(new ConsoleLogHandler(new ConsoleLogEntryFormatter(), LogLevel.ALL, console));

        System.setOut(new PrintStream(new LogOutputStream(logger, LogLevel.INFO), true));
        System.setErr(new PrintStream(new LogOutputStream(logger, LogLevel.ERROR), true));

        Master master = new Master(logger, console);
        master.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping...");
            console.close();
            logger.close();
        }));

    }

}
