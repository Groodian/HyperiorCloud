package de.groodian.hyperiorcloud.command.commands;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.command.Command;

public class StopServiceCommand extends Command {

    public StopServiceCommand() {
        super("Stops a service.", "stopservice", "stops");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (!Master.getInstance().getServiceHandler().stopService(args[0])) {
                Master.getInstance().getLogger().command("This serviceId was not found!");
            }
        } else {
            Master.getInstance().getLogger().command("Usage: stop <serviceId>");
        }
    }

}
