package de.groodian.hyperiorcloud.command.commands;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.command.Command;
import de.groodian.hyperiorcloud.service.Service;

public class ListServicesCommand extends Command {

    public ListServicesCommand() {
        super("Shows all services.", "listservices", "lists");
    }

    @Override
    public void execute(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("A list of all services:");
        for (Service service : Master.getInstance().getServiceHandler().getServices()) {
            stringBuilder
                    .append("\n")
                    .append(service.getId())
                    .append(": ")
                    .append(service.getServiceStatus());
        }
        if (Master.getInstance().getServiceHandler().getServices().isEmpty()) {
            stringBuilder.append("\nNo services running");
        }
        Master.getInstance().getLogger().command(stringBuilder.toString());
    }

}
