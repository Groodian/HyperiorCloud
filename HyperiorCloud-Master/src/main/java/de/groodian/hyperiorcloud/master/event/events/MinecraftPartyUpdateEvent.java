package de.groodian.hyperiorcloud.master.event.events;

import de.groodian.hyperiorcloud.master.event.Event;
import de.groodian.hyperiorcloud.master.service.Service;

public class MinecraftPartyUpdateEvent implements Event {

    private Service service;
    private String gameState;
    private int onlinePlayers;
    private int maxPlayers;

    public MinecraftPartyUpdateEvent(Service service, String gameState, int onlinePlayers, int maxPlayers) {
        this.service = service;
        this.gameState = gameState;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }

    public Service getService() {
        return service;
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
