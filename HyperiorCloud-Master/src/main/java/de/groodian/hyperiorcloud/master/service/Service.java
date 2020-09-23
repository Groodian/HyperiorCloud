package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.logging.Logger;
import de.groodian.hyperiorcloud.master.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Service {

    protected ServiceHandler serviceHandler;
    protected Logger logger = Master.getInstance().getLogger();

    protected String group;
    protected int groupNumber;
    protected int port;
    protected String stopCommand;

    private Connection connection;
    private ServiceStatus serviceStatus;
    private Process process;

    protected String sourcePath;
    protected String destinationPath;
    protected File sourceFile;
    protected File destinationFile;

    private long startStartTime;

    public Service(ServiceHandler serviceHandler, String group, int port, int groupNumber, String stopCommand) {
        this.serviceHandler = serviceHandler;
        this.group = group;
        this.groupNumber = groupNumber;
        this.port = port;
        this.stopCommand = stopCommand;

        long startPrepareTime = System.currentTimeMillis();
        logger.info("[" + getId() + "] Preparing service...");

        new Thread(() -> {

            sourcePath = "templates/" + group + "/";
            destinationPath = "temp/" + group + "/" + groupNumber + "/";

            sourceFile = new File(sourcePath);
            destinationFile = new File(destinationPath);

            if (!sourceFile.exists()) {
                sourceFile.mkdirs();
            }

            if (!destinationFile.exists()) {
                destinationFile.mkdirs();
            }

            checkFiles0();
            copyFiles();
            setProperties0();

            logger.info("[" + getId() + "] Service prepared. (" + (System.currentTimeMillis() - startPrepareTime) + "ms)");

            start();

        }).start();

    }

    private void checkFiles0() {
        logger.debug("[" + getId() + "] Checking files...");
        serviceStatus = ServiceStatus.CHECKING_FILES;
        checkFiles();
    }

    protected abstract List<String> checkFiles();

    private void copyFiles() {
        logger.debug("[" + getId() + "] Copying files...");
        serviceStatus = ServiceStatus.COPYING_FILES;

        try {
            FileUtil.copyFolder(sourceFile, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setProperties0() {
        logger.debug("[" + getId() + "] Setting properties...");
        serviceStatus = ServiceStatus.SETTING_PROPERTIES;
        setProperties();
    }

    protected abstract void setProperties();

    private void start() {
        startStartTime = System.currentTimeMillis();
        serviceStatus = ServiceStatus.STARTING;

        try {

            if (serviceHandler.getOs() == OS.WINDOWS) {
                logger.info("[" + getId() + "] Starting service... (using start.bat)");
                process = new ProcessBuilder().command("cmd /c start start.bat").directory(destinationFile).start();
            } else if (serviceHandler.getOs() == OS.LINUX) {
                logger.info("[" + getId() + "] Starting service... (using start.sh)");
                process = new ProcessBuilder().command("sh -c ./start.sh").directory(destinationFile).start();
            } else {
                logger.fatal("[" + getId() + "] Could not start the service because the OS is unknown!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        if (serviceStatus != ServiceStatus.STARTING && serviceStatus != ServiceStatus.CONNECTED) {
            logger.debug("[" + getId() + "] Prevented to stop the service in the status: " + serviceStatus);
            return;
        }

        long stopStartTime = System.currentTimeMillis();
        logger.info("[" + getId() + "] Stopping service...");
        serviceStatus = ServiceStatus.STOPPING;

        new Thread(() -> {

            int exitValue = stop0();
            saveLog();
            delete();

            connection.close();

            logger.info("[" + getId() + "] Service stopped. Exit value: " + exitValue + " (" + (System.currentTimeMillis() - stopStartTime) + "ms)");

        }).start();

    }

    private int stop0() {
        executeCommand(stopCommand);

        try {

            if (process.waitFor(10, TimeUnit.SECONDS)) {
                return process.exitValue();
            }

            process.destroyForcibly();
            process.waitFor();

            return process.exitValue();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private void saveLog() {
        logger.debug("[" + getId() + "] Saving log...");
        serviceStatus = ServiceStatus.SAVING_LOG;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd---HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        try {
            Files.copy(new File(destinationPath + "logs/latest.log").toPath(), new File("logs/" + group + "/" + dateTimeFormatter.format(now) + "---" + groupNumber + ".log").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        logger.debug("[" + getId() + "] Deleting service...");
        serviceStatus = ServiceStatus.DELETING;

        FileUtil.deleteFolder(destinationFile);
    }

    public void connectionLost() {
        stop();
        serviceHandler.removeService(this);
    }

    public void setConnection(Connection connection) {
        logger.info("[" + getId() + "] Service started. (" + (System.currentTimeMillis() - startStartTime) + "ms)");
        serviceStatus = ServiceStatus.CONNECTED;
        this.connection = connection;
    }

    public void executeCommand(String command) {
        logger.debug("[" + getId() + "] Executing command: " + command);
        new Thread(() -> {

            try {
                OutputStreamWriter out = new OutputStreamWriter(process.getOutputStream());
                out.write(command);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public String getGroup() {
        return group;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public String getId() {
        return group + "-" + groupNumber;
    }

    public Connection getConnection() {
        return connection;
    }

}
