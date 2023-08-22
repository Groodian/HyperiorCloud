package de.groodian.hyperiorcloud.listerner;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.event.EventListener;
import de.groodian.hyperiorcloud.event.Listener;
import de.groodian.hyperiorcloud.event.events.LobbyUpdateEvent;
import de.groodian.hyperiorcloud.event.events.MinecraftPartyUpdateEvent;
import de.groodian.hyperiorcloud.event.events.ServiceConnectedEvent;
import de.groodian.hyperiorcloud.event.events.ServiceDisconnectedEvent;
import de.groodian.hyperiorcloud.service.Service;
import de.groodian.hyperiorcloud.service.connections.LobbyServiceConnection;
import de.groodian.hyperiorcloud.service.connections.MinecraftPartyServiceConnection;
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
        if (!e.getService().getGroup().equalsIgnoreCase("PROXY")) {
            Master.getInstance().getServiceHandler().broadcastToGroup("PROXY", new DataPackage(
                    "CONNECTED",
                    e.getService().getGroup(),
                    e.getService().getGroupNumber(),
                    e.getService().getConnection().getClientAddress(),
                    e.getService().getPort()
            ));
        } else {
            for (Service service : Master.getInstance().getServiceHandler().getServices()) {
                if (service.getConnection() != null) {
                    if (!service.getGroup().equalsIgnoreCase("PROXY")) {
                        Master.getInstance().getServiceHandler().sendTo(e.getService(), new DataPackage(
                                "CONNECTED",
                                service.getGroup(),
                                service.getGroupNumber(),
                                service.getConnection().getClientAddress(),
                                service.getPort()
                        ));
                    }
                }
            }
        }

        if (e.getService().getGroup().equalsIgnoreCase("LOBBY")) {
            for (Service service : Master.getInstance().getServiceHandler().getServices()) {
                if (service.getConnection() != null) {
                    if (service.getGroup().equalsIgnoreCase("LOBBY")) {
                        LobbyServiceConnection lobbyServiceConnection = (LobbyServiceConnection) service.getConnection();
                        Master.getInstance().getServiceHandler().sendTo(e.getService(), new DataPackage(
                                "UPDATE",
                                service.getGroup(),
                                service.getGroupNumber(),
                                lobbyServiceConnection.getOnlinePlayers(),
                                lobbyServiceConnection.getMaxPlayers()
                        ));
                    } else if (service.getGroup().equalsIgnoreCase("MINECRAFTPARTY")) {
                        MinecraftPartyServiceConnection minecraftPartyServiceConnection = (MinecraftPartyServiceConnection) service.getConnection();
                        Master.getInstance().getServiceHandler().sendTo(e.getService(), new DataPackage(
                                "UPDATE",
                                service.getGroup(),
                                service.getGroupNumber(),
                                minecraftPartyServiceConnection.getGameState(),
                                minecraftPartyServiceConnection.getOnlinePlayers(),
                                minecraftPartyServiceConnection.getMaxPlayers()
                        ));
                    }
                }
            }
        }
    }

    @EventListener
    public void handleServiceDisconnect(ServiceDisconnectedEvent e) {
        if (e.getService().getGroup().equalsIgnoreCase("MINECRAFTPARTY") || e.getService().getGroup().equalsIgnoreCase("LOBBY")) {
            Master.getInstance().getServiceHandler().broadcastToGroup("LOBBY", new DataPackage(
                    "DISCONNECTED",
                    e.getService().getGroup(),
                    e.getService().getGroupNumber()
            ));
        }

        if (!e.getService().getGroup().equalsIgnoreCase("PROXY")) {
            Master.getInstance().getServiceHandler().broadcastToGroup("PROXY", new DataPackage(
                    "DISCONNECTED",
                    e.getService().getGroup(),
                    e.getService().getGroupNumber()
            ));
        }
    }

}
