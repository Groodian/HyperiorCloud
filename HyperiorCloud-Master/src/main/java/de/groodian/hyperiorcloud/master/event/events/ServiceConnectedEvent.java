package de.groodian.hyperiorcloud.master.event.events;

import de.groodian.hyperiorcloud.master.event.Event;
import de.groodian.hyperiorcloud.master.service.Connection;

public class ServiceConnectedEvent implements Event {

    private Connection connection;
    private String group;
    private int groupNumber;

    public ServiceConnectedEvent(Connection connection, String group, int groupNumber) {
        this.connection = connection;
        this.group = group;
        this.groupNumber = groupNumber;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getGroup() {
        return group;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

}
