package de.groodian.hyperiorcloud.master.service.services;

import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SpigotService extends Service {

    public SpigotService(ServiceHandler serviceHandler, String group, int groupNumber, int port) {
        super(serviceHandler, group, groupNumber, port, "stop", Arrays.asList("java", "-Xmx3G", "-jar", "spigot.jar"));
    }

    @Override
    protected List<String> checkFiles() {
        List<String> missingFiles = new ArrayList<>();

        if (!new File(sourcePath, "server.properties").exists())
            missingFiles.add("server.properties");
        if (!new File(sourcePath, "spigot.jar").exists())
            missingFiles.add("spigot.jar");
        if (!new File(sourcePath, "eula.txt").exists())
            missingFiles.add("eula.txt");

        return missingFiles;
    }

    @Override
    protected boolean setProperties() {
        try {
            Properties properties = new Properties();
            FileInputStream in = new FileInputStream(new File(destinationPath, "server.properties"));
            FileOutputStream out = new FileOutputStream(new File(destinationPath, "server.properties"));
            properties.load(in);
            properties.setProperty("server-port", String.valueOf(port));
            properties.setProperty("server-name", String.valueOf(groupNumber));
            properties.setProperty("motd", getId());
            properties.store(out, "Edited by HyperiorCloud");
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            errorRoutine("Could not set service properties!", e);
            return false;
        }
    }

}
