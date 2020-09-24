package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.network.DataPackage;

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

                Socket tempSocket = null;

                try {

                    Master.getInstance().getLogger().debug("[ServiceServer] Waiting for connection...");
                    tempSocket = serverSocket.accept();
                    Master.getInstance().getLogger().debug("[ServiceServer] Connected to service: " + tempSocket.getRemoteSocketAddress());

                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(tempSocket.getInputStream()));
                    Object pack = ois.readObject();

                    if (pack instanceof DataPackage) {

                        DataPackage datapackage = (DataPackage) pack;
                        String header = datapackage.get(0).toString();

                        if (header.equalsIgnoreCase("LOGIN")) {
                            serviceHandler.newConnection(tempSocket, ois, datapackage.get(1).toString(), (int) datapackage.get(2));
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
                } catch (Exception e) {
                    e.printStackTrace();
                    if (tempSocket != null) {
                        try {
                            tempSocket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }

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
