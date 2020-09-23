package de.groodian.hyperiorcloud.master.service.services;

import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;

import java.util.ArrayList;
import java.util.List;

public class BungeecordService extends Service {

    public BungeecordService(ServiceHandler serviceHandler, String group, int groupNumber, int port) {
        super(serviceHandler, group, groupNumber, port, "end");
    }

    @Override
    protected List<String> checkFiles() {
        List<String> missingFiles = new ArrayList<>();

        return missingFiles;
    }

    @Override
    protected boolean setProperties() {
        return false;
    }

}
