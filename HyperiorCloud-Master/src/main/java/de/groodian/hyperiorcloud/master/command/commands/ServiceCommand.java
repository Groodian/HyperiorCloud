package de.groodian.hyperiorcloud.master.command.commands;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.command.Command;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;

public class ServiceCommand extends Command {

    private static final String USAGE = "Usage: " +
            "\nservice start [serviceType] [serviceId]" +
            "\nservice stop [serviceId]" +
            "\nservice list";

    private ServiceHandler serviceHandler;

    public ServiceCommand(ServiceHandler serviceHandler) {
        super("Manage services.", "service");
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("A list of all services:");
                for (Service service : serviceHandler.getServices()) {
                    stringBuilder
                            .append("\n")
                            .append(service.getId())
                            .append(": ")
                            .append(service.getServiceStatus());
                }
                if (serviceHandler.getServices().isEmpty()) {
                    stringBuilder.append("\nNo services running");
                }
                Master.getInstance().getLogger().command(stringBuilder.toString());
            } else {
                Master.getInstance().getLogger().command(USAGE);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("stop")) {
                if (!serviceHandler.stopService(args[1])) {
                    Master.getInstance().getLogger().command("This serviceId was not found!");
                }
            } else {
                Master.getInstance().getLogger().command(USAGE);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("start")) {
                serviceHandler.startService(args[1], args[2]);
            } else {
                Master.getInstance().getLogger().command(USAGE);
            }
        } else {
            Master.getInstance().getLogger().command(USAGE);
        }
    }

}
