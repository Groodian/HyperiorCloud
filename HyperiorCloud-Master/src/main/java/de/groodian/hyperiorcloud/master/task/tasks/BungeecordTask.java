package de.groodian.hyperiorcloud.master.task.tasks;

import de.groodian.hyperiorcloud.master.service.ServiceType;
import de.groodian.hyperiorcloud.master.task.Task;

public class BungeecordTask extends Task {

    public BungeecordTask() {
        super(ServiceType.BUNGEECORD, "BUNGEECORD");
    }

    @Override
    protected boolean startCondition() {
        return false;
    }

}
