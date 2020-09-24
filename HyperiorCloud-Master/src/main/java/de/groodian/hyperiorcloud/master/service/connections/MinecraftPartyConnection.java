package de.groodian.hyperiorcloud.master.service.connections;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.Connection;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.network.DataPackage;

import java.io.ObjectInputStream;
import java.net.Socket;

public class MinecraftPartyConnection extends Connection {

    private String gameState = "Starting...";
    private int onlinePlayers = 0;
    private int maxPlayers = 0;

    public MinecraftPartyConnection(Service service, Socket socket, ObjectInputStream ois) {
        super(service, socket, ois);
    }

    @Override
    protected void handleDataPackage(DataPackage datapackage) {
        String header = datapackage.get(0).toString();
        if (header.equalsIgnoreCase("SERVER_INFO")) {
            gameState = datapackage.get(1).toString();
            onlinePlayers = (int) datapackage.get(2);
            maxPlayers = (int) datapackage.get(3);
        } else {
            Master.getInstance().getLogger().warning("[" + service.getId() + "] Unknowen header: " + header);
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
