package de.groodian.hyperiorcloud.task;

import de.groodian.hyperiorcloud.service.ServiceType;

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
