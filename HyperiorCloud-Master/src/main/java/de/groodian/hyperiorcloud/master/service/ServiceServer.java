package de.groodian.hyperiorcloud.master.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

                    System.out.println("[Server] Waiting for connection...");
                    Socket tempSocket = serverSocket.accept();
                    System.out.println("[Server] Connected to client: " + tempSocket.getRemoteSocketAddress());

                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(tempSocket.getInputStream()));
                    Object pack = ois.readObject();
                    ois.close();

                    if (pack instanceof Datapackage) {

                        Datapackage datapackage = (Datapackage) pack;
                        String header = datapackage.get(0).toString();

                        if (header.equalsIgnoreCase("LOGIN")) {
                            serviceHandler.newConnection(tempSocket, datapackage.get(1).toString(), (int) datapackage.get(2));
                        } else {
                            System.err.println("[Server] Unknown header: " + header);
                            tempSocket.close();
                        }

                    } else {
                        System.err.println("[Server] Unknown pack: " + pack);
                        tempSocket.close();
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        });
        listeningThread.setName("server");
        listeningThread.start();
    }

    public void start() {
        System.out.println("[Server] Server starting...");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("[Server] Could not start the Server. Is the port free?");
        }
        System.out.println("[Server] Server started.");
        startListening();
    }

    public void stop() {
        System.out.println("[Server] Server stopping...");
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listeningThread.interrupt();
        System.out.println("[Server] Server stopped.");
    }

}
