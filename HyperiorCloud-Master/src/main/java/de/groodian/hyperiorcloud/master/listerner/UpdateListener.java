package de.groodian.hyperiorcloud.master.listerner;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.event.EventListener;
import de.groodian.hyperiorcloud.master.event.Listener;
import de.groodian.hyperiorcloud.master.event.events.LobbyUpdateEvent;
import de.groodian.hyperiorcloud.master.event.events.MinecraftPartyUpdateEvent;
import de.groodian.hyperiorcloud.master.event.events.ServiceConnectedEvent;
import de.groodian.hyperiorcloud.master.event.events.ServiceDisconnectedEvent;
import de.groodian.network.DataPackage;

public class UpdateListener implements Listener {

    @EventListener
    public void handleMinecraftPartyUpdate(MinecraftPartyUpdateEvent e) {
        Master.getInstance().getServiceHandler().broadcastToGroup("LOBBY", new DataPackage(
                "UPDATE",
                e.getService().getGroup(),
                e.getService().getGroupNumber(),
                e.getGameState(),
                e.getOnlinePlayers(),
                e.getMaxPlayers()
        ));
    }

    @EventListener
    public void handleLobbyUpdate(LobbyUpdateEvent e) {
        Master.getInstance().getServiceHandler().broadcastToGroup("LOBBY", new DataPackage(
                "UPDATE",
                e.getService().getGroup(),
                e.getService().getGroupNumber(),
                e.getOnlinePlayers(),
                e.getMaxPlayers()
        ));
    }

    @EventListener
    public void handleServiceConnect(ServiceConnectedEvent e) {
        Master.getInstance().getServiceHandler().broadcastToGroup("BUNGEECORD", new DataPackage(
                "CONNECTED",
                e.getService().getGroup(),
                e.getService().getGroupNumber(),
                e.getService().getConnection().getClientAddress(),
                e.getService().getPort()
        ));
    }

    @EventListener
    public void handleServiceDisconnect(ServiceDisconnectedEvent e) {
        if (e.getService().getGroup().equals("MINECRAFTPARTY") || e.getService().getGroup().equals("LOBBY")) {
            Master.getInstance().getServiceHandler().broadcastToGroup("LOBBY", new DataPackage(
                    "DISCONNECTED",
                    e.getService().getGroup(),
                    e.getService().getGroupNumber()
            ));
        }

        Master.getInstance().getServiceHandler().broadcastToGroup("BUNGEECORD", new DataPackage(
                "DISCONNECTED",
                e.getService().getGroup(),
                e.getService().getGroupNumber()
        ));
    }

}
