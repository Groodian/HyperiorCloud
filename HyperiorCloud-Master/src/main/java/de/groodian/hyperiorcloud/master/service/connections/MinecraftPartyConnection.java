package de.groodian.hyperiorcloud.master.service.connections;

import de.groodian.hyperiorcloud.master.service.Connection;
import de.groodian.hyperiorcloud.master.service.Datapackage;
import de.groodian.hyperiorcloud.master.service.Service;

import java.net.Socket;

public class MinecraftPartyConnection extends Connection {

    public MinecraftPartyConnection(Service service, Socket socket) {
        super(service, socket);
    }

    @Override
    protected void handleDatapackage(Datapackage datapackage) {

    }

}
