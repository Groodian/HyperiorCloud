package de.groodian.hyperiorcloud.master.command.commands;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.command.Command;
import de.groodian.hyperiorcloud.master.command.CommandManager;

public class HelpCommand extends Command {

    private CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("Shows all commands with description.", "help");
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("A list of all commands:\n");
        for (Command command : commandManager.getCommands()) {
            boolean first = true;
            for (String commandName : command.getNames()) {
                if (!first) {
                    stringBuilder.append("&7, ");
                } else {
                    first = false;
                }
                stringBuilder
                        .append("&a")
                        .append(commandName);
            }
            stringBuilder
                    .append(" &8- &f")
                    .append(command.getDescription())
                    .append("\n");
        }
        Master.getInstance().getLogger().command(stringBuilder.toString());
    }

}
