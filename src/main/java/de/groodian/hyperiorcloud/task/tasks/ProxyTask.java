package de.groodian.hyperiorcloud.task.tasks;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.service.Service;
import de.groodian.hyperiorcloud.service.ServiceType;
import de.groodian.hyperiorcloud.task.Task;

public class ProxyTask extends Task {

    private static final int MAX_SERVICES = 1;

    public ProxyTask() {
        super(ServiceType.VELOCITY, "PROXY");
    }

    @Override
    protected boolean startCondition() {
        int services = 0;
        for (Service service : Master.getInstance().getServiceHandler().getServices()) {
            if (service.getGroup().equalsIgnoreCase("PROXY")) {
                services++;
            }
        }

        if (services < MAX_SERVICES) {
            return true;
        }

        return false;
    }

}
