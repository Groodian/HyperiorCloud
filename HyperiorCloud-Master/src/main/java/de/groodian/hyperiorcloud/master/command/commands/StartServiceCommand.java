package de.groodian.hyperiorcloud.master.command.commands;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.command.Command;

public class StartServiceCommand extends Command {

    public StartServiceCommand() {
        super("Starts a service.", "startservice", "starts");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            Master.getInstance().getServiceHandler().startService(args[0], args[1]);
        } else {
            Master.getInstance().getLogger().command("Usage: start <serviceType> <templateName>");
        }
    }

}
