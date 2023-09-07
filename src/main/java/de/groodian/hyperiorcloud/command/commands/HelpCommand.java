package de.groodian.hyperiorcloud.command.commands;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.command.Command;
import de.groodian.hyperiorcloud.command.CommandManager;
import de.groodian.hyperiorcloud.console.ConsoleColor;
import org.jline.utils.AttributedStringBuilder;

public class HelpCommand extends Command {

    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("Shows all commands with description.", "help");
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String[] args) {
        AttributedStringBuilder attributedStringBuilder = new AttributedStringBuilder();
        attributedStringBuilder.append("A list of all commands:");
        for (Command command : commandManager.getCommands()) {
            attributedStringBuilder.append("\n");
            boolean first = true;
            for (String commandName : command.getNames()) {
                if (!first) {
                    attributedStringBuilder.append(", ", ConsoleColor.GRAY.getStyle());
                } else {
                    first = false;
                }
                attributedStringBuilder.append(commandName, ConsoleColor.GREEN.getStyle());
            }
            attributedStringBuilder
                    .append(" - ", ConsoleColor.DARK_GRAY.getStyle())
                    .append(command.getDescription(), ConsoleColor.WHITE.getStyle());
        }

        Master.getInstance().getLogger().command(attributedStringBuilder.toAnsi());
    }

}
