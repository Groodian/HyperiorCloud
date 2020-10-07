package de.groodian.hyperiorcloud.master.command.commands;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.command.Command;

public class ExecuteCommandOnServiceCommand extends Command {

    public ExecuteCommandOnServiceCommand() {
        super("Executes a command on a service.", "execmdonservice", "execmdons");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 2) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (stringBuilder.length() == 0) {
                    stringBuilder.append(args[i]);
                } else {
                    stringBuilder.append(" ").append(args[i]);
                }
            }
            if (!Master.getInstance().getServiceHandler().executeCommandOnService(args[0], stringBuilder.toString())) {
                Master.getInstance().getLogger().command("This serviceId was not found!");
            }
        } else {
            Master.getInstance().getLogger().command("Usage: execmd <serviceId> <command>");
        }
    }

}
