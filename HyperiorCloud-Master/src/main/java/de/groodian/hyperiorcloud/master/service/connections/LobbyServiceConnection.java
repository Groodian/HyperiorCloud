package de.groodian.hyperiorcloud.master.service.connections;

import de.groodian.hyperiorcloud.master.service.Connection;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceConnection;
import de.groodian.network.DataPackage;

public class LobbyServiceConnection extends ServiceConnection {

    public LobbyServiceConnection(Connection connection, Service service) {
        super(connection, service);
    }

    @Override
    protected void handleDataPackage(DataPackage datapackage) {

    }

}
