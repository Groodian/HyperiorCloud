package de.groodian.hyperiorcloud.service.connections;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.event.events.LobbyUpdateEvent;
import de.groodian.hyperiorcloud.service.Connection;
import de.groodian.hyperiorcloud.service.Service;
import de.groodian.hyperiorcloud.service.ServiceConnection;
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

            Master.getInstance().getEventHandler().callEvent(new LobbyUpdateEvent(service, onlinePlayers, maxPlayers));
        } else {
            Master.getInstance().getLogger().warning("[" + service.getId() + "] Unknown header: " + header);
        }
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
