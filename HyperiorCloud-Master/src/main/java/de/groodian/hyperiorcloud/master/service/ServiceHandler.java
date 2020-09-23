package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.connections.BungeecordConnection;
import de.groodian.hyperiorcloud.master.service.connections.LobbyConnection;
import de.groodian.hyperiorcloud.master.service.connections.MinecraftPartyConnection;
import de.groodian.hyperiorcloud.master.service.services.SpigotService;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServiceHandler {

    private List<Service> services;

    private OS os;
    private Thread thread;

    public ServiceHandler() {
        services = new ArrayList<>();

        os = OS.getOS();
        if (os == OS.UNKNOWN) {
            Master.getInstance().getLogger().fatal("[ServiceHandler] Could not successfully create because the OS is unknown!");
        }

    }

    public void start() {
        thread = new Thread(() -> {

            while (!thread.isInterrupted()) {

                int groupNumber = getGroupNumber("MinecraftParty");
                if (groupNumber == -1) {
                    Master.getInstance().getLogger().error("[ServiceHandler] Could not find a group number!");
                }

                int port = getPort();
                if (port == -1) {
                    Master.getInstance().getLogger().error("[ServiceHandler] Could not find a port!");
                }

                services.add(new SpigotService(this, "MinecraftParty", groupNumber, port));

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
        thread.setName("service-handler");
        thread.start();
    }

    public void stop() {
        Master.getInstance().getLogger().info("[ServiceHandler] Stopping...");
        if (thread != null) {
            thread.interrupt();
        }
        for (Service service : services) {
            service.stop();
        }
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
                    Master.getInstance().getLogger().error("[ServiceHandler] Unknown group: " + group);
                }
            }
        }
    }

    public void removeService(Service service) {
        if (services.contains(service)) {
            services.remove(service);
        } else {
            Master.getInstance().getLogger().error("[ServiceHandler] Could not remove service!", new IllegalArgumentException());
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

    private int getGroupNumber(String group) {
        List<Integer> usedGroupNumbers = new ArrayList<>();
        for (Service service : services) {
            if (service.getGroup().equalsIgnoreCase(group)) {
                usedGroupNumbers.add(service.getGroupNumber());
            }
        }
        for (int groupNumber = 1; groupNumber < 100000; groupNumber++) {
            if (!usedGroupNumbers.contains(groupNumber)) {
                return groupNumber;
            }
        }

        return -1;
    }

    private int getPort() {
        List<Integer> usedPorts = new ArrayList<>();
        for (Service service : services) {
            usedPorts.add(service.getPort());
        }
        for (int port = 25566; port <= 65535; port++) {
            if (!usedPorts.contains(port)) {
                return port;
            }
        }

        return -1;
    }

    public OS getOs() {
        return os;
    }

}
