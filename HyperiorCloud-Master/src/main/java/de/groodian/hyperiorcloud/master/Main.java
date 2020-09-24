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

        Thread shutdownThread = new Thread(new Runnable() {
            @Override
            public void run() {

                logger.info("Stopping HyperiorCloud-Master...");
                master.stop();

                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                console.close();
                logger.close();

            }
        });
        shutdownThread.setName("shutdown");

        Runtime.getRuntime().addShutdownHook(shutdownThread);

    }

}
