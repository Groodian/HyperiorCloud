package de.groodian.hyperiorcloud.master.service.connections;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.Connection;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceConnection;
import de.groodian.network.DataPackage;

public class LobbyServiceConnection extends ServiceConnection {

    private int onlinePlayers = 0;
    private int maxPlayers = 0;

    public LobbyServiceConnection(Connection connection, Service service) {
        super(connection, service);
    }

    @Override
    protected void handleDataPackage(DataPackage datapackage) {
        String header = datapackage.get(0).toString();
        if (header.equalsIgnoreCase("SERVICE_INFO")) {
            onlinePlayers = (int) datapackage.get(1);
            maxPlayers = (int) datapackage.get(2);
        } else {
            Master.getInstance().getLogger().warning("[" + service.getId() + "] Unknowen header: " + header);
        }
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
