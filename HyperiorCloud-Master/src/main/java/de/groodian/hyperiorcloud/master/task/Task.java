package de.groodian.hyperiorcloud.master.task;

import de.groodian.hyperiorcloud.master.service.ServiceType;

public abstract class Task {

    private ServiceType serviceType;
    private String group;

    public Task(ServiceType serviceType, String group) {
        this.serviceType = serviceType;
        this.group = group;
    }

    protected abstract boolean startCondition();

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getGroup() {
        return group;
    }

}
