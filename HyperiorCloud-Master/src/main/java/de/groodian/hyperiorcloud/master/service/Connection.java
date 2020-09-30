package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.event.events.ServiceConnectedEvent;
import de.groodian.network.DataPackage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Thread thread;

    public Connection(Socket socket) {
        this.socket = socket;

        waitForServiceInformation();
    }

    private void waitForServiceInformation() {
        thread = new Thread(() -> {

            try {

                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                Object pack = ois.readObject();

                if (pack instanceof DataPackage) {

                    DataPackage datapackage = (DataPackage) pack;
                    String header = datapackage.get(0).toString();

                    if (header.equalsIgnoreCase("LOGIN")) {
                        oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                        Master.getInstance().getEventHandler().callEvent(new ServiceConnectedEvent(this, datapackage.get(1).toString(), (int) datapackage.get(2)));
                    } else {
                        Master.getInstance().getLogger().warning("[" + socket.getRemoteSocketAddress() + "] Unknown header: " + header + " Closing connection...");
                        socket.close();
                    }

                } else {
                    Master.getInstance().getLogger().warning("[" + socket.getRemoteSocketAddress() + "] Unknown pack: " + pack + " Closing connection...");
                    socket.close();
                }

            } catch (Exception e) {
                Master.getInstance().getLogger().warning("[" + socket.getRemoteSocketAddress() + "] Could not get service information. Closing connection...", e);
                try {
                    socket.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        thread.setName(socket.getInetAddress().toString() + "-connection");
        thread.start();
    }

    public void close() {
        try {
            if (thread != null)
                thread.interrupt();
            if (ois != null)
                ois.close();
            if (oos != null)
                oos.close();
            if (socket != null)
                socket.close();
        } catch (SocketException e) {
            // do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

}
