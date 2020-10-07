package de.groodian.hyperiorcloud.master;

import de.groodian.hyperiorcloud.master.command.CommandManager;
import de.groodian.hyperiorcloud.master.command.commands.ClearCommand;
import de.groodian.hyperiorcloud.master.command.commands.ExecuteCommandOnServiceCommand;
import de.groodian.hyperiorcloud.master.command.commands.ExitCommand;
import de.groodian.hyperiorcloud.master.command.commands.HelpCommand;
import de.groodian.hyperiorcloud.master.command.commands.ListServicesCommand;
import de.groodian.hyperiorcloud.master.command.commands.StartServiceCommand;
import de.groodian.hyperiorcloud.master.command.commands.StopServiceCommand;
import de.groodian.hyperiorcloud.master.console.Console;
import de.groodian.hyperiorcloud.master.event.EventHandler;
import de.groodian.hyperiorcloud.master.listerner.UpdateListener;
import de.groodian.hyperiorcloud.master.logging.Logger;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;
import de.groodian.hyperiorcloud.master.service.ServiceServer;
import de.groodian.hyperiorcloud.master.task.TaskHandler;
import de.groodian.hyperiorcloud.master.task.tasks.BungeecordTask;
import de.groodian.hyperiorcloud.master.task.tasks.LobbyTask;
import de.groodian.hyperiorcloud.master.task.tasks.MinecraftPartyTask;

public class Master {

    private static Master instance;

    private Logger logger;
    private Console console;
    private CommandManager commandManager;
    private ServiceHandler serviceHandler;
    private ServiceServer serviceServer;
    private TaskHandler taskHandler;
    private EventHandler eventHandler;

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

        eventHandler = new EventHandler();
        eventHandler.registerListener(new UpdateListener());

        commandManager = new CommandManager();
        console.setCommandManager(commandManager);

        serviceHandler = new ServiceHandler();
        eventHandler.registerListener(serviceHandler);

        taskHandler = new TaskHandler();
        taskHandler.registerTask(new MinecraftPartyTask());
        taskHandler.registerTask(new LobbyTask());
        taskHandler.registerTask(new BungeecordTask());

        serviceServer = new ServiceServer(4444);
        if (serviceServer.start()) {
            taskHandler.start();
        }

        commandManager.registerCommand(new HelpCommand(commandManager));
        commandManager.registerCommand(new ExitCommand());
        commandManager.registerCommand(new ClearCommand(console));
        commandManager.registerCommand(new StartServiceCommand());
        commandManager.registerCommand(new StopServiceCommand());
        commandManager.registerCommand(new ListServicesCommand());
        commandManager.registerCommand(new ExecuteCommandOnServiceCommand());

        logger.info("Loaded.");
        logger.info("Use 'help' for help.");
    }

    public void stop() {
        logger.info("Stopping HyperiorCloud-Master...");
        console.stopReading();
        Thread shutdownThread = new Thread(() -> {
            taskHandler.stop();
            serviceServer.stop();
            serviceHandler.stop(25000);

            logger.info("Good bye ;)");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Runtime.getRuntime().exit(0);
        });
        shutdownThread.setName("shutdown");
        shutdownThread.start();
    }

    public static Master getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public ServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

}
