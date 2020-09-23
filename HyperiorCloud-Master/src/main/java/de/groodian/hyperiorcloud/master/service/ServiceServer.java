package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServiceServer {

    private ServiceHandler serviceHandler;

    private int port;
    private ServerSocket serverSocket;
    private Thread listeningThread;

    public ServiceServer(int port, ServiceHandler serviceHandler) {
        this.port = port;
        this.serviceHandler = serviceHandler;
    }

    private void startListening() {
        listeningThread = new Thread(() -> {

            while (!listeningThread.isInterrupted()) {

                try {

                    Master.getInstance().getLogger().debug("[ServiceServer] Waiting for connection...");
                    Socket tempSocket = serverSocket.accept();
                    Master.getInstance().getLogger().debug("[ServiceServer] Connected to service: " + tempSocket.getRemoteSocketAddress());

                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(tempSocket.getInputStream()));
                    Object pack = ois.readObject();
                    ois.close();

                    if (pack instanceof Datapackage) {

                        Datapackage datapackage = (Datapackage) pack;
                        String header = datapackage.get(0).toString();

                        if (header.equalsIgnoreCase("LOGIN")) {
                            serviceHandler.newConnection(tempSocket, datapackage.get(1).toString(), (int) datapackage.get(2));
                        } else {
                            Master.getInstance().getLogger().warning("[ServiceServer] Unknown header: " + header);
                            tempSocket.close();
                        }

                    } else {
                        Master.getInstance().getLogger().warning("[ServiceServer] Unknown pack: " + pack);
                        tempSocket.close();
                    }

                } catch (SocketException e) {
                    Master.getInstance().getLogger().debug("[ServiceServer] Socket closed.");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        });
        listeningThread.setName("service-server");
        listeningThread.start();
    }

    public boolean start() {
        Master.getInstance().getLogger().info("[ServiceServer] Starting...");
        try {
            serverSocket = new ServerSocket(port);
            Master.getInstance().getLogger().info("[ServiceServer] Started.");
            startListening();
            return true;
        } catch (IOException e) {
            Master.getInstance().getLogger().fatal("[ServiceServer] Could not be started. Is the port free?");
        }

        return false;
    }

    public void stop() {
        Master.getInstance().getLogger().info("[ServiceServer] Stopping...");

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (listeningThread != null) {
            listeningThread.interrupt();
        }

        Master.getInstance().getLogger().info("[ServiceServer] Stopped.");
    }

}
