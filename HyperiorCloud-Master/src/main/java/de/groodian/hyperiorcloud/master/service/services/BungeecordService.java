package de.groodian.hyperiorcloud.master.service.services;

import de.groodian.hyperiorcloud.master.service.Service;
import de.groodian.hyperiorcloud.master.service.ServiceHandler;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BungeecordService extends Service {

    public BungeecordService(ServiceHandler serviceHandler, String group, int groupNumber, int port) {
        super(serviceHandler, group, groupNumber, port, "end", Arrays.asList("java", "-Xmx6G", "-jar", "bungeecord.jar"));
    }

    @Override
    protected List<String> checkFiles() {
        List<String> missingFiles = new ArrayList<>();

        if (!new File(sourcePath, "config.yml").exists())
            missingFiles.add("config.yml");
        if (!new File(sourcePath, "bungeecord.jar").exists())
            missingFiles.add("bungeecord.jar");

        return missingFiles;
    }

    @Override
    protected boolean setProperties() {
        try {
            File file = new File(sourcePath, "config.yml");
            List<String> lines = Files.readAllLines(file.toPath());
            List<String> editedLines = new ArrayList<>();
            for (String line : lines) {
                if (line.startsWith("  host:")) {
                    line = "  host: 0.0.0.0:" + port;
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
