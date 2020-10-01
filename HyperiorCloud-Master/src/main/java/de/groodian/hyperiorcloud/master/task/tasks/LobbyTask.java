package de.groodian.hyperiorcloud.master.task.tasks;

import de.groodian.hyperiorcloud.master.service.ServiceType;
import de.groodian.hyperiorcloud.master.task.Task;

public class LobbyTask extends Task {

    public LobbyTask() {
        super(ServiceType.SPIGOT, "LOBBY");
    }

    @Override
    protected boolean startCondition() {
        return false;
    }

}
