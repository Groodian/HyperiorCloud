package de.groodian.hyperiorcloud.master.task.tasks;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceType;
import de.groodian.hyperiorcloud.master.task.Task;

public class BungeecordTask extends Task {

    private static final int MAX_SERVICES = 1;

    public BungeecordTask() {
        super(ServiceType.BUNGEECORD, "BUNGEECORD");
    }

    @Override
    protected boolean startCondition() {
        int services = 0;
        for (Service service : Master.getInstance().getServiceHandler().getServices()) {
            if (service.getGroup().equalsIgnoreCase("BUNGEECORD")) {
                services++;
            }
        }

        if (services < MAX_SERVICES) {
            return true;
        }

        return false;
    }

}
