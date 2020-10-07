package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.event.EventListener;
import de.groodian.hyperiorcloud.master.event.Listener;
import de.groodian.hyperiorcloud.master.event.events.PreServiceConnectedEvent;
import de.groodian.hyperiorcloud.master.service.connections.BungeecordServiceConnection;
import de.groodian.hyperiorcloud.master.service.connections.LobbyServiceConnection;
import de.groodian.hyperiorcloud.master.service.connections.MinecraftPartyServiceConnection;
import de.groodian.hyperiorcloud.master.service.services.BungeecordService;
import de.groodian.hyperiorcloud.master.service.services.SpigotService;
import de.groodian.network.DataPackage;

import java.util.ArrayList;
import java.util.List;

public class ServiceHandler implements Listener {

    private List<Service> services;
    private List<Service> servicesToRemove;

    public ServiceHandler() {
        services = new ArrayList<>();
        servicesToRemove = new ArrayList<>();
    }

    public void stop(long timeout) {
        Master.getInstance().getLogger().info("[ServiceHandler] Stopping...");
        for (Service service : getServices()) {
            service.stop();
        }
        Master.getInstance().getLogger().important("[ServiceHandler] Waiting for all services to stop...");
        long timeWaited = 0;
        while (true) {

            for (Service service : servicesToRemove) {
                services.remove(service);
            }

            if (services.isEmpty()) {
                Master.getInstance().getLogger().info("[ServiceHandler] All services stopped.");
                break;
            } else {
                if (timeWaited < timeout) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timeWaited += 200;
                } else {
                    Master.getInstance().getLogger().warning("[ServiceHandler] Could not stop all services.");
                    break;
                }
            }

        }

    }

    public void startService(String serviceType, String group) {
        try {
            startService(ServiceType.valueOf(serviceType.toUpperCase()), group);
        } catch (IllegalArgumentException e) {
            Master.getInstance().getLogger().error("[ServiceHandler] Unknown service type: " + serviceType);
        }
    }

    public void startService(ServiceType serviceType, String group) {
        int groupNumber = getGroupNumber(group);
        if (groupNumber == -1) {
            Master.getInstance().getLogger().error("[ServiceHandler] Could not find a group number!");
        }

        int port = getPort();
        if (port == -1) {
            Master.getInstance().getLogger().error("[ServiceHandler] Could not find a port!");
        }

        switch (serviceType) {
            case SPIGOT:
                services.add(new SpigotService(this, group, groupNumber, port));
                break;
            case BUNGEECORD:
                services.add(new BungeecordService(this, group, groupNumber, 25565));
                break;
            default:
                Master.getInstance().getLogger().error("[ServiceHandler] Unknown service type: " + serviceType);
                break;
        }

    }

    public boolean stopService(String serviceId) {
        for (Service service : getServices()) {
            if (service.getId().equalsIgnoreCase(serviceId)) {
                service.stop();
                return true;
            }
        }
        return false;
    }

    @EventListener
    public void handlePreServiceConnected(PreServiceConnectedEvent e) {
        for (Service service : getServices()) {
            if (service.getId().equalsIgnoreCase(e.getGroup() + "-" + e.getGroupNumber())) {
                if (e.getGroup().equalsIgnoreCase("BUNGEECORD")) {
                    service.setConnection(new BungeecordServiceConnection(e.getConnection(), service));
                } else if (e.getGroup().equalsIgnoreCase("MINECRAFTPARTY")) {
                    service.setConnection(new MinecraftPartyServiceConnection(e.getConnection(), service));
                } else if (e.getGroup().equalsIgnoreCase("LOBBY")) {
                    service.setConnection(new LobbyServiceConnection(e.getConnection(), service));
                } else {
                    Master.getInstance().getLogger().error("[ServiceHandler] Unknown group: " + e.getGroup());
                }
                return;
            }
        }

        Master.getInstance().getLogger().error("[ServiceHandler] Unknown serviceId: " + e.getGroup() + "-" + e.getGroupNumber());
    }

    public void removeService(Service service) {
        if (services.contains(service)) {
            servicesToRemove.add(service);
        } else {
            Master.getInstance().getLogger().error("[ServiceHandler] Could not remove service!", new IllegalArgumentException());
        }
    }

    public List<Service> getServices() {
        for (Service service : servicesToRemove) {
            services.remove(service);
        }

        return services;
    }

    public boolean executeCommandOnService(String serviceId, String command) {
        for (Service service : getServices()) {
            if (service.getId().equalsIgnoreCase(serviceId)) {
                service.executeCommand(command);
                return true;
            }
        }
        return false;
    }

    public void broadcast(DataPackage pack) {
        new Thread(() -> {

            for (Service service : getServices()) {
                sendMessage(service, pack);
            }

        }).start();
    }

    public void broadcastToGroup(String group, DataPackage pack) {
        new Thread(() -> {

            for (Service service : getServices()) {
                if (service.getGroup().equalsIgnoreCase(group)) {
                    sendMessage(service, pack);
                }
            }

        }).start();
    }

    public void sendTo(Service service, DataPackage pack) {
        new Thread(() -> {

            sendMessage(service, pack);

        }).start();
    }

    private void sendMessage(Service service, DataPackage pack) {
        if (service.getConnection() != null) {
            Master.getInstance().getLogger().debug("[" + service.getId() + "] Sending pack: " + pack);
            service.getConnection().sendMessage(pack);
        }
    }

    private int getGroupNumber(String group) {
        List<Integer> usedGroupNumbers = new ArrayList<>();
        for (Service service : getServices()) {
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
        for (Service service : getServices()) {
            usedPorts.add(service.getPort());
        }
        for (int port = 25566; port <= 65535; port++) {
            if (!usedPorts.contains(port)) {
                return port;
            }
        }

        return -1;
    }

}
