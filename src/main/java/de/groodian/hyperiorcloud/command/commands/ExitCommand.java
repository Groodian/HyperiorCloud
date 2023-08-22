package de.groodian.hyperiorcloud.command.commands;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.command.Command;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("Shutdowns the programm.", "exit", "quit", "leave", "stop", "end");
    }

    @Override
    public void execute(String[] args) {
        Master.getInstance().stop();
    }
}
