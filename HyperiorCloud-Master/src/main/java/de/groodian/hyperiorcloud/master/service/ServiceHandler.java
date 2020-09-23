package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.connections.BungeecordConnection;
import de.groodian.hyperiorcloud.master.service.connections.LobbyConnection;
import de.groodian.hyperiorcloud.master.service.connections.MinecraftPartyConnection;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServiceHandler {

    private OS os;

    private List<Service> services;

    public ServiceHandler() {
        os = OS.getOS();
        if (os == OS.UNKNOWN) {
            Master.getInstance().getLogger().fatal("Could not successfully create the service handler because the OS is unknown!");
        }
        services = new ArrayList<>();
    }

    public void newConnection(Socket socket, String group, int groupNumber) {
        for (Service service : services) {
            if (service.getId().equalsIgnoreCase(group + "-" + groupNumber)) {
                if (group.equalsIgnoreCase("BUNGEECORD")) {
                    service.setConnection(new BungeecordConnection(service, socket));
                } else if (group.equalsIgnoreCase("MINECRAFTPARTY")) {
                    service.setConnection(new MinecraftPartyConnection(service, socket));
                } else if (group.equalsIgnoreCase("LOBBY")) {
                    service.setConnection(new LobbyConnection(service, socket));
                } else {
                    Master.getInstance().getLogger().error("Unknown group: " + group);
                }
            }
        }
    }

    public void removeService(Service service) {
        if (services.contains(service)) {
            services.remove(service);
        } else {
            Master.getInstance().getLogger().error("Could not remove service!", new IllegalArgumentException());
        }
    }

    public void broadcast(Datapackage pack) {
        new Thread(() -> {

            for (Service service : services) {
                sendMessage(service, pack);
            }

        }).start();
    }

    public void broadcastToGroup(String group, Datapackage pack) {
        new Thread(() -> {

            for (Service service : services) {
                if (service.getGroup().equalsIgnoreCase(group)) {
                    sendMessage(service, pack);
                }
            }

        }).start();
    }

    public void sendTo(Service service, Datapackage pack) {
        new Thread(() -> {

            sendMessage(service, pack);

        }).start();
    }

    private void sendMessage(Service service, Datapackage pack) {
        service.getConnection().sendMessage(pack);
    }

    public OS getOs() {
        return os;
    }

}
