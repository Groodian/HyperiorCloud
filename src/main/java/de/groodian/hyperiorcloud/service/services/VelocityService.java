package de.groodian.hyperiorcloud.service.services;

import de.groodian.hyperiorcloud.service.Service;
import de.groodian.hyperiorcloud.service.ServiceHandler;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VelocityService extends Service {

    public VelocityService(ServiceHandler serviceHandler, String group, int groupNumber, int port) {
        super(serviceHandler, group, groupNumber, port, "end",
                Arrays.asList("java", "-Xms512M", "-Xmx1G", "-XX:+UseG1GC", "-XX:G1HeapRegionSize=4M", "-XX:+UnlockExperimentalVMOptions",
                        "-XX:+ParallelRefProcEnabled", "-XX:+AlwaysPreTouch", "-jar", "velocity.jar"));
    }

    @Override
    protected List<String> checkFiles() {
        List<String> missingFiles = new ArrayList<>();

        checkFilesHelper(missingFiles, "velocity.jar");
        checkFilesHelper(missingFiles, "velocity.toml");
        checkFilesHelper(missingFiles, "forwarding.secret");

        return missingFiles;
    }

    @Override
    protected boolean setProperties() {
        try {
            File file = new File(destinationPath, "velocity.toml");
            List<String> lines = Files.readAllLines(file.toPath());
            List<String> editedLines = new ArrayList<>();
            for (String line : lines) {
                if (line.startsWith("bind = ")) {
                    line = "bind = \"0.0.0.0:" + port + "\"";
                } else if (line.startsWith("motd = ")) {
                    line = "motd = \"" + groupNumber + "\"";
                }
                editedLines.add(line);
            }
            PrintWriter writer = new PrintWriter(file);
            for (String line : editedLines) {
                writer.write(line + "\n");
                writer.flush();
            }
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
