package de.groodian.hyperiorcloud.master.command.commands;

import de.groodian.hyperiorcloud.master.command.Command;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("Shutdowns the programm.", "exit", "quit", "leave", "stop", "end");
    }

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
}
