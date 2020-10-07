package de.groodian.hyperiorcloud.master.event.events;

import de.groodian.hyperiorcloud.master.event.Event;
import de.groodian.hyperiorcloud.master.service.Connection;

public class PreServiceConnectedEvent implements Event {

    private Connection connection;
    private String group;
    private int groupNumber;
    private String clientAddress;
    private int clientPort;

    public PreServiceConnectedEvent(Connection connection, String group, int groupNumber, String clientAddress, int clientPort) {
        this.connection = connection;
        this.group = group;
        this.groupNumber = groupNumber;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
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

    public String getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

}
