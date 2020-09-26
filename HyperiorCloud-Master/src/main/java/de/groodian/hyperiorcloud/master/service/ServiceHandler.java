package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.connections.BungeecordServiceConnection;
import de.groodian.hyperiorcloud.master.service.connections.LobbyServiceConnection;
import de.groodian.hyperiorcloud.master.service.connections.MinecraftPartyServiceConnection;
import de.groodian.hyperiorcloud.master.service.services.BungeecordService;
import de.groodian.hyperiorcloud.master.service.services.SpigotService;
import de.groodian.network.DataPackage;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServiceHandler {

    private List<Service> services;
    private List<Service> servicesToRemove;

    private Thread thread;

    public ServiceHandler() {
        services = new ArrayList<>();
        servicesToRemove = new ArrayList<>();
    }

    public void start() {
        thread = new Thread(() -> {

            while (!thread.isInterrupted()) {


                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    Master.getInstance().getLogger().debug("[ServiceHandler] Canceled start loop.");
                }

            }

        });
        thread.setName("service-handler");
        thread.start();
    }

    public void stop(long timeout) {
        Master.getInstance().getLogger().info("[ServiceHandler] Stopping...");
        if (thread != null) {
            thread.interrupt();
        }
        for (Service service : services) {
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
                    Master.getInstance().getLogger().info("[ServiceHandler] Could not stop all services.");
                    break;
                }
            }

        }

    }

    public void startService(String group) {
        for (Service service : servicesToRemove) {
            services.remove(service);
        }

        int groupNumber = getGroupNumber(group);
        if (groupNumber == -1) {
            Master.getInstance().getLogger().error("[ServiceHandler] Could not find a group number!");
        }

        int port = getPort();
        if (port == -1) {
            Master.getInstance().getLogger().error("[ServiceHandler] Could not find a port!");
        }

        if (group.equalsIgnoreCase("BUNGEECORD")) {
            services.add(new BungeecordService(this, group, groupNumber, port));
        } else {
            services.add(new SpigotService(this, group, groupNumber, port));
        }
    }

    public boolean stopService(String serviceId) {
        for (Service service : services) {
            if (service.getId().equalsIgnoreCase(serviceId)) {
                service.stop();
                return true;
            }
        }
        return false;
    }

    public void newConnection(Connection connection, String group, int groupNumber) {
        for (Service service : services) {
            if (service.getId().equalsIgnoreCase(group + "-" + groupNumber)) {
                if (group.equalsIgnoreCase("BUNGEECORD")) {
                    service.setConnection(new BungeecordServiceConnection(connection, service));
                } else if (group.equalsIgnoreCase("MINECRAFTPARTY")) {
                    service.setConnection(new MinecraftPartyServiceConnection(connection, service));
                } else if (group.equalsIgnoreCase("LOBBY")) {
                    service.setConnection(new LobbyServiceConnection(connection, service));
                } else {
                    Master.getInstance().getLogger().error("[ServiceHandler] Unknown group: " + group);
                }
                break;
            }
        }
    }

    public void removeService(Service service) {
        if (services.contains(service)) {
            servicesToRemove.add(service);
        } else {
            Master.getInstance().getLogger().error("[ServiceHandler] Could not remove service!", new IllegalArgumentException());
        }
    }

    public void broadcast(DataPackage pack) {
        new Thread(() -> {

            for (Service service : services) {
                sendMessage(service, pack);
            }

        }).start();
    }

    public void broadcastToGroup(String group, DataPackage pack) {
        new Thread(() -> {

            for (Service service : services) {
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

}
