package de.groodian.hyperiorcloud.master.task.tasks;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceType;
import de.groodian.hyperiorcloud.master.task.Task;

public class LobbyTask extends Task {

    private static final int MAX_SERVICES = 1;

    public LobbyTask() {
        super(ServiceType.SPIGOT, "LOBBY");
    }

    @Override
    protected boolean startCondition() {
        int services = 0;
        for (Service service : Master.getInstance().getServiceHandler().getServices()) {
            if (service.getGroup().equalsIgnoreCase("LOBBY")) {
                services++;
            }
        }

        if (services < MAX_SERVICES) {
            return true;
        }

        return false;
    }

}
