package de.groodian.hyperiorcloud.task.tasks;

import de.groodian.hyperiorcloud.Master;
import de.groodian.hyperiorcloud.service.Service;
import de.groodian.hyperiorcloud.service.ServiceType;
import de.groodian.hyperiorcloud.service.connections.MinecraftPartyServiceConnection;
import de.groodian.hyperiorcloud.task.Task;

public class MinecraftPartyTask extends Task {

    private static final int MAX_SERVICES = 5;
    private static final int MAX_SERVICES_IN_LOBBY_STATE = 1;

    public MinecraftPartyTask() {
        super(ServiceType.PAPER, "MINECRAFTPARTY");
    }

    @Override
    protected boolean startCondition() {
        int serversInLobbyState = 0;
        int serversStarting = 0;
        int totalServers = 0;

        for (Service service : Master.getInstance().getServiceHandler().getServices()) {
            if (service.getGroup().equalsIgnoreCase("MINECRAFTPARTY")) {
                if (service.getConnection() != null) {
                    if (service.getConnection() instanceof MinecraftPartyServiceConnection) {
                        MinecraftPartyServiceConnection connection = (MinecraftPartyServiceConnection) service.getConnection();
                        if (connection.getGameState().equalsIgnoreCase("LOBBY")) {
                            serversInLobbyState++;
                        } else if (connection.getGameState().equalsIgnoreCase(MinecraftPartyServiceConnection.DEFAULT_GAME_STATE)) {
                            serversStarting++;
                        }
                    }
                } else {
                    serversStarting++;
                }
                totalServers++;
            }
        }

        if (serversInLobbyState + serversStarting < MAX_SERVICES_IN_LOBBY_STATE && totalServers < MAX_SERVICES) {

            return true;

        }

        return false;
    }

}
