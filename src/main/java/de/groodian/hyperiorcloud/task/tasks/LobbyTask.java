package de.groodian.hyperiorcloud.task.tasks;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.service.Service;
import de.groodian.hyperiorcloud.service.ServiceType;
import de.groodian.hyperiorcloud.task.Task;

public class LobbyTask extends Task {

    private static final int MAX_SERVICES = 1;

    public LobbyTask() {
        super(ServiceType.PAPER, "LOBBY");
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
