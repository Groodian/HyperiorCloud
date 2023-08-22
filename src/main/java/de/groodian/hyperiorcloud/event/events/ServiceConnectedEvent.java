package de.groodian.hyperiorcloud.event.events;

import de.groodian.hyperiorcloud.event.Event;
import de.groodian.hyperiorcloud.service.Service;

public class ServiceConnectedEvent implements Event {

    private Service service;

    public ServiceConnectedEvent(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

}
