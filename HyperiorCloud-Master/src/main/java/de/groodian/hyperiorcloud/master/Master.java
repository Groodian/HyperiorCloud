package de.groodian.hyperiorcloud.master;

import de.groodian.hyperiorcloud.master.command.CommandManager;
import de.groodian.hyperiorcloud.master.command.commands.ClearCommand;
import de.groodian.hyperiorcloud.master.command.commands.ExitCommand;
import de.groodian.hyperiorcloud.master.command.commands.HelpCommand;
import de.groodian.hyperiorcloud.master.console.Console;
import de.groodian.hyperiorcloud.master.logging.Logger;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;
import de.groodian.hyperiorcloud.master.service.ServiceServer;

public class Master {

    private static Master instance;

    private Logger logger;
    private Console console;
    private CommandManager commandManager;
    private ServiceHandler serviceHandler;
    private ServiceServer serviceServer;

    public Master(Logger logger, Console console) {
        instance = this;

        this.logger = logger;
        this.console = console;
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

        commandManager = new CommandManager();
        console.setCommandManager(commandManager);

        commandManager.registerCommand(new HelpCommand(commandManager));
        commandManager.registerCommand(new ExitCommand());
        commandManager.registerCommand(new ClearCommand(console));

        serviceHandler = new ServiceHandler();
        serviceServer = new ServiceServer(4444, serviceHandler);
        if (serviceServer.start()) {
            serviceHandler.start();
        }

        logger.info("Loaded.");
        logger.info("Use 'help' for help.");
    }

    public void stop() {
        serviceServer.stop();
        serviceHandler.stop();
    }

    public static Master getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

}