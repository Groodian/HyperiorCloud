package de.groodian.hyperiorcloud.master.service.services;

import de.groodian.hyperiorcloud.master.service.OS;
import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SpigotService extends Service {

    public SpigotService(ServiceHandler serviceHandler, String group, int groupNumber, int port) {
        super(serviceHandler, group, groupNumber, port, "stop");
    }

    @Override
    protected List<String> checkFiles() {
        List<String> missingFiles = new ArrayList<>();

        if (!new File(destinationPath, "server.properties").exists())
            missingFiles.add("server.properties");
        if (!new File(destinationPath, "spigot.jar").exists())
            missingFiles.add("spigot.jar");
        if (!new File(destinationPath, "eula.txt").exists())
            missingFiles.add("eula.txt");

        if (serviceHandler.getOs() == OS.WINDOWS) {
            if (!new File(destinationPath, "start.bat").exists())
                missingFiles.add("start.bat");
        } else if (serviceHandler.getOs() == OS.LINUX) {
            if (!new File(destinationPath, "start.sh").exists())
                missingFiles.add("start.sh");
        } else {
            logger.fatal("[" + getId() + "] Could not check the files because the OS is unknown!");
        }

        return missingFiles;
    }

    @Override
    protected void setProperties() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(destinationPath, "server.properties")));
            properties.setProperty("port", String.valueOf(port));
            properties.setProperty("server-name", getId());
            properties.store(new FileOutputStream(new File(destinationPath, "server.properties")), "Edited by HyperiorCloud");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
