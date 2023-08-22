package de.groodian.hyperiorcloud.command.commands;

import de.groodian.hyperiorcloud.command.Command;
import de.groodian.hyperiorcloud.console.Console;

public class ClearCommand extends Command {

    private Console console;

    public ClearCommand(Console console) {
        super("Clears the screen.", "clear", "cls");
        this.console = console;
    }

    @Override
    public void execute(String[] args) {
        console.clearScreen();
    }

}
