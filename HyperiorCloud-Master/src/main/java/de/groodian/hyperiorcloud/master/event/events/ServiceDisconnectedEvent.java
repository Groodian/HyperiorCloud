package de.groodian.hyperiorcloud.master.event.events;

import de.groodian.hyperiorcloud.master.event.Event;
import de.groodian.hyperiorcloud.master.service.Service;

public class ServiceDisconnectedEvent implements Event {

    private Service service;

    public ServiceDisconnectedEvent(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

}
