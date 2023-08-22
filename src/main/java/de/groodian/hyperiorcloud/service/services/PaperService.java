package de.groodian.hyperiorcloud.service.services;

import de.groodian.hyperiorcloud.service.Service;
import de.groodian.hyperiorcloud.service.ServiceHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PaperService extends Service {

    public PaperService(ServiceHandler serviceHandler, String group, int groupNumber, int port) {
        super(serviceHandler, group, groupNumber, port, "stop", Arrays.asList("java", "-Xms2G", "-Xmx4G", "-jar", "paper.jar", "--nogui"));
    }

    @Override
    protected List<String> checkFiles() {
        List<String> missingFiles = new ArrayList<>();

        checkFilesHelper(missingFiles, "server.properties");
        checkFilesHelper(missingFiles, "paper.jar");
        checkFilesHelper(missingFiles, "eula.txt");

        return missingFiles;
    }

    @Override
    protected boolean setProperties() {
        try {
            Properties properties = new Properties();

            FileInputStream in = new FileInputStream(new File(destinationPath, "server.properties"));
            properties.load(in);
            in.close();

            properties.setProperty("server-port", String.valueOf(port));
            properties.setProperty("motd", String.valueOf(groupNumber));

            FileOutputStream out = new FileOutputStream(new File(destinationPath, "server.properties"));
            properties.store(out, "Edited by HyperiorCloud");
            out.close();

            return true;
        } catch (Exception e) {
            errorRoutine("Could not set service properties!", e);
            return false;
        }
    }

}
