package de.groodian.hyperiorcloud.master.command.commands;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.command.Command;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;

public class ServiceCommand extends Command {

    private static final String USAGE = "Usage: service <start/stop> <serviceId>";

    private ServiceHandler serviceHandler;

    public ServiceCommand(ServiceHandler serviceHandler) {
        super("Manage services.", "service");
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("start")) {
                serviceHandler.startService(args[1]);
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (!serviceHandler.stopService(args[1])) {
                    Master.getInstance().getLogger().command("This serviceId was not found!");
                }
            } else {
                Master.getInstance().getLogger().command(USAGE);
            }
        } else {
            Master.getInstance().getLogger().command(USAGE);
        }
    }

}
