package de.groodian.hyperiorcloud.command.commands;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.command.Command;
import de.groodian.hyperiorcloud.command.CommandManager;

public class HelpCommand extends Command {

    private CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("Shows all commands with description.", "help");
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("A list of all commands:");
        for (Command command : commandManager.getCommands()) {
            stringBuilder.append("\n");
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
                    .append(command.getDescription());
        }
        Master.getInstance().getLogger().command(stringBuilder.toString());
    }

}
