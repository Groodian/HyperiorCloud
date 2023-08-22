package de.groodian.hyperiorcloud.service;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.event.events.ServiceDisconnectedEvent;
import de.groodian.network.DataPackage;

public abstract class ServiceConnection {

    protected Service service;

    private Connection connection;
    private Thread thread;

    public ServiceConnection(Connection connection, Service service) {
        this.connection = connection;
        this.service = service;

        startListening();
    }

    protected abstract void handleDataPackage(DataPackage datapackage);

    private void startListening() {
        thread = new Thread(() -> {

            while (!thread.isInterrupted()) {

                try {

                    Object pack = connection.getOis().readObject();
                    if (pack instanceof DataPackage) {
                        Master.getInstance().getLogger().debug("[" + service.getId() + "] Pack received: " + pack);
                        handleDataPackage((DataPackage) pack);
                    } else {
                        Master.getInstance().getLogger().warning("[" + service.getId() + "] Unknown pack: " + pack);
                    }

                } catch (Exception e) {
                    Master.getInstance()
                            .getLogger()
                            .debug("[" + service.getId() + "] An connection error occurred, stopping service...", e);
                    service.stop();
                    Master.getInstance().getEventHandler().callEvent(new ServiceDisconnectedEvent(service));
                    break;
                }

            }

        });
        thread.setName(service.getId().toLowerCase() + "-connection");
        thread.start();
    }

    public void sendMessage(DataPackage pack) {
        if (service.getServiceStatus() != ServiceStatus.CONNECTED) {
            Master.getInstance()
                    .getLogger()
                    .debug("[" + service.getId() + "] Prevented to send a message in the status: " + service.getServiceStatus());
            return;
        }

        try {
            connection.getOos().writeObject(pack);
            connection.getOos().flush();
        } catch (Exception e) {
            Master.getInstance().getLogger().warning("[" + service.getId() + "] The message " + pack + " could not be send!", e);
        }
    }

    public String getClientAddress() {
        return connection.getSocket().getInetAddress().getHostAddress();
    }

    public void close() {
        connection.close();
        thread.interrupt();
    }

}
