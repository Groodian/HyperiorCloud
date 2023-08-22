package de.groodian.hyperiorcloud.service;

import de.groodian.hyperiorcloud.Master;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServiceServer {

    private int port;
    private ServerSocket serverSocket;
    private Thread listeningThread;

    public ServiceServer(int port) {
        this.port = port;
    }

    private void startListening() {
        listeningThread = new Thread(() -> {

            while (!listeningThread.isInterrupted()) {

                try {

                    Master.getInstance().getLogger().debug("[ServiceServer] Waiting for connection...");
                    Socket tempSocket = serverSocket.accept();
                    Master.getInstance()
                            .getLogger()
                            .debug("[ServiceServer] Connected to service: " + tempSocket.getInetAddress().getHostAddress() + ":" +
                                   tempSocket.getPort());

                    new Connection(tempSocket);

                } catch (SocketException e) {
                    Master.getInstance().getLogger().debug("[ServiceServer] Socket closed.");
                } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listeningThread != null) {
            listeningThread.interrupt();
        }

        Master.getInstance().getLogger().info("[ServiceServer] Stopped.");
    }

}
