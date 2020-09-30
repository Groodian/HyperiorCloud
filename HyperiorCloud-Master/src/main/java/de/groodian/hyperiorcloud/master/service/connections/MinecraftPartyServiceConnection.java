package de.groodian.hyperiorcloud.master.service.connections;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.event.events.MinecraftPartyUpdateEvent;
import de.groodian.hyperiorcloud.master.service.Connection;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceConnection;
import de.groodian.network.DataPackage;

public class MinecraftPartyServiceConnection extends ServiceConnection {

    public static final String DEFAULT_GAME_STATE = "Starting...";

    private String gameState = DEFAULT_GAME_STATE;
    private int onlinePlayers = 0;
    private int maxPlayers = 0;

    public MinecraftPartyServiceConnection(Connection connection, Service service) {
        super(connection, service);
    }

    @Override
    protected void handleDataPackage(DataPackage datapackage) {
        String header = datapackage.get(0).toString();
        if (header.equalsIgnoreCase("SERVICE_INFO")) {
            gameState = datapackage.get(1).toString();
            onlinePlayers = (int) datapackage.get(2);
            maxPlayers = (int) datapackage.get(3);

            Master.getInstance().getEventHandler().callEvent(new MinecraftPartyUpdateEvent(service, gameState, onlinePlayers, maxPlayers));
        } else {
            Master.getInstance().getLogger().warning("[" + service.getId() + "] Unknown header: " + header);
        }
    }

    public String getGameState() {
        return gameState;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
