package de.groodian.hyperiorcloud.master;

import de.groodian.hyperiorcloud.master.logging.LogEntry;
import de.groodian.hyperiorcloud.master.logging.LogLevel;
import de.groodian.hyperiorcloud.master.logging.Logger;

public class Master {

    private static Master instance;

    private Logger logger;

    public Master(Logger logger) {
        this.logger = logger;
        instance = this;
    }

    public void start() {
        logger.info("HyperiorCore-Master is loading...");
        logger.error("This is a test error message!");
        logger.registerHandler(null);
        logger.log(new LogEntry(LogLevel.FATAL, System.currentTimeMillis(), "This is a test message with additional infos!", null, Thread.currentThread(), null));

        System.out.println("It also works with the standard System.out.println() method!");
        System.err.println("And with the System.err.println() method!");

        for (int i = 0; i < 25000; i++) {
            logger.important(i + "");
        }

        logger.close();
    }

    public static Master getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

}
