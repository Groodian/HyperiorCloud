package de.groodian.hyperiorcloud.master.command;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.logging.LogHandler;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private List<Command> commands;

    public CommandManager() {
        commands = new ArrayList<>();
    }

    public void registerCommand(Command command) {
        if (command != null) {
            commands.add(command);
        } else {
            Master.getInstance().getLogger().error("Could not register command!", new IllegalArgumentException());
        }
    }

    public void unregisterCommand(LogHandler logHandler) {
        if (commands.contains(logHandler)) {
            commands.remove(logHandler);
        } else {
            Master.getInstance().getLogger().error("Could not remove the command!", new IllegalArgumentException());
        }
    }

    public void callCommand(String name, String[] args) {
        for (Command command : commands) {
            for (String commandName : command.getNames()) {
                if (commandName.equalsIgnoreCase(name)) {
                    command.execute(args);
                    return;
                }
            }
        }
        Master.getInstance().getLogger().command("Unknown command use 'help' for help.");
    }

    public void callCommand(String line) {
        if (line != null && !line.isEmpty()) {
            String[] splittedLine = line.split(" ");
            String[] args = new String[0];
            if (splittedLine.length > 1) {
                args = new String[splittedLine.length - 1];
                for (int i = 0; i < splittedLine.length - 1; i++) {
                    args[i] = splittedLine[i + 1];
                }
            }
            callCommand(splittedLine[0], args);
        } else {
            Master.getInstance().getLogger().error("Could not call the command!", new IllegalArgumentException());
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

}
