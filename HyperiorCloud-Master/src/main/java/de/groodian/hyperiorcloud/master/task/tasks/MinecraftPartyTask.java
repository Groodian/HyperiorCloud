package de.groodian.hyperiorcloud.master.task.tasks;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.connections.MinecraftPartyServiceConnection;
import de.groodian.hyperiorcloud.master.task.Task;

public class MinecraftPartyTask extends Task {

    private static final int MAX_SERVERS = 5;
    private static final int MAX_SERVERS_IN_LOBBY_STATE = 1;

    public MinecraftPartyTask() {
        super("MINECRAFTPARTY");
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

        if (serversInLobbyState + serversStarting < MAX_SERVERS_IN_LOBBY_STATE && totalServers < MAX_SERVERS) {

            return true;

        }

        return false;
    }

}
