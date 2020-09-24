package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.network.DataPackage;

import java.io.*;
import java.net.Socket;

public abstract class Connection {

    protected Service service;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Thread thread;

    public Connection(Service service, Socket socket, ObjectInputStream ois) {
        this.service = service;
        this.socket = socket;
        this.ois = ois;

        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        startListening();
    }

    protected abstract void handleDataPackage(DataPackage datapackage);

    private void startListening() {
        thread = new Thread(() -> {

            while (!thread.isInterrupted()) {

                try {

                    Object pack = ois.readObject();
                    if (pack instanceof DataPackage) {
                        handleDataPackage((DataPackage) pack);
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

    public void sendMessage(DataPackage pack) {
        try {
            oos.writeObject(pack);
            oos.flush();
        } catch (IOException e) {
            Master.getInstance().getLogger().warning("[" + service.getId() + "] The message " + pack + " could not be send!");
        }
    }

    public boolean isAlive() {
        return socket.isConnected() && ois != null && oos != null;
    }

    public void close() {
        try {
            if (ois != null)
                ois.close();
            if (oos != null)
                oos.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    public Service getService() {
        return service;
    }

}
