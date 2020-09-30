package de.groodian.hyperiorcloud.master.event.events;

import de.groodian.hyperiorcloud.master.event.Event;
import de.groodian.hyperiorcloud.master.service.Service;

public class LobbyUpdateEvent implements Event {

    private Service service;
    private int onlinePlayers;
    private int maxPlayers;

    public LobbyUpdateEvent(Service service, int onlinePlayers, int maxPlayers) {
        this.service = service;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }

    public Service getService() {
        return service;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
