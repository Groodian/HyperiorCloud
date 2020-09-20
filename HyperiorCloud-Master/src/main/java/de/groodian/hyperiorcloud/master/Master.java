package de.groodian.hyperiorcloud.master;

import de.groodian.hyperiorcloud.master.console.Console;
import de.groodian.hyperiorcloud.master.logging.Logger;

public class Master {

    private static Master instance;

    private Logger logger;
    private Console console;

    public Master(Logger logger, Console console) {
        this.logger = logger;
        this.console = console;
        instance = this;
    }

    public void start() {
        logger.info("&b\n" +
                "    __  __                      _            ________                __\n" +
                "   / / / /_  ______  ___  _____(_)___  _____/ ____/ /___  __  ______/ /\n" +
                "  / /_/ / / / / __ \\/ _ \\/ ___/ / __ \\/ ___/ /   / / __ \\/ / / / __  / \n" +
                " / __  / /_/ / /_/ /  __/ /  / / /_/ / /  / /___/ / /_/ / /_/ / /_/ /  \n" +
                "/_/ /_/\\__, / .___/\\___/_/  /_/\\____/_/   \\____/_/\\____/\\__,_/\\__,_/   \n" +
                "      /____/_/                                                         \n");
        logger.info("HyperiorCloud-Master is loading...");
        logger.fatal("This is a test fatal message!");
        logger.error("This is a test error message!");
        logger.warning("This is a test warning message!");
        logger.important("This is a test important message!");

        System.out.println("It also works with the standard System.out.println() method!");
        System.err.println("And with the System.err.println() method!");

    }

    public static Master getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public Console getConsole() {
        return console;
    }

}
