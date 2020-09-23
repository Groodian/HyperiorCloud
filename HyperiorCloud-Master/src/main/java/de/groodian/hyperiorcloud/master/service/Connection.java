package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;

import java.io.*;
import java.net.Socket;

public abstract class Connection {

    private Connection connection;

    private Service service;
    private Socket socket;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Thread thread;

    private String state;
    private int onlinePlayers;
    private int maxPlayers;

    public Connection(Service service, Socket socket) {
        this.connection = this;
        this.service = service;
        this.socket = socket;

        try {
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        startListening();
    }

    protected abstract void handleDatapackage(Datapackage datapackage);

    private void startListening() {
        thread = new Thread(() -> {

            while (!thread.isInterrupted()) {

                try {

                    Object pack = ois.readObject();
                    if (pack instanceof Datapackage) {
                        handleDatapackage((Datapackage) pack);
                    } else {
                        Master.getInstance().getLogger().warning("[" + service.getId() + "] Unknown pack: " + pack);
                    }

                } catch (ClassNotFoundException | IOException e) {
                    Master.getInstance().getLogger().debug("[" + service.getId() + "] The service is unreachable, stopping service...");
                    service.stop();
                    break;
                }

            }

        });
        thread.setName(service.getId().toLowerCase() + "-connection");
        thread.start();
    }

    public void sendMessage(Datapackage pack) {
        try {
            oos.writeObject(pack);
            oos.flush();
        } catch (IOException e) {
            Master.getInstance().getLogger().warning("[" + service.getId() + "] The message " + pack + " could not be send!");
        }
    }

    public void close() {
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    public String getState() {
        return state;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
