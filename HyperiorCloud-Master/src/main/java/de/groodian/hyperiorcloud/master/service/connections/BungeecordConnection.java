package de.groodian.hyperiorcloud.master.service.connections;

import de.groodian.hyperiorcloud.master.service.Connection;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.network.DataPackage;

import java.io.ObjectInputStream;
import java.net.Socket;

public class BungeecordConnection extends Connection {

    public BungeecordConnection(Service service, Socket socket, ObjectInputStream ois) {
        super(service, socket, ois);
    }

    @Override
    protected void handleDataPackage(DataPackage datapackage) {

    }

}
