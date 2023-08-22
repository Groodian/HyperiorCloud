package de.groodian.hyperiorcloud.event.events;

import de.groodian.hyperiorcloud.event.Event;
import de.groodian.hyperiorcloud.service.Service;

public class ServiceDisconnectedEvent implements Event {

    private Service service;

    public ServiceDisconnectedEvent(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

}
