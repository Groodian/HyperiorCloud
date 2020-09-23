package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.logging.Logger;
import de.groodian.hyperiorcloud.master.util.FileUtil;

import java.io.File;
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

    public Service(ServiceHandler serviceHandler, String group, int groupNumber, int port, String stopCommand) {
        this.serviceHandler = serviceHandler;
        this.group = group;
        this.groupNumber = groupNumber;
        this.port = port;
        this.stopCommand = stopCommand;

        long startPrepareTime = System.currentTimeMillis();
        logger.info("[" + getId() + "] Preparing service...");

        Thread prepareThread = new Thread(() -> {

            sourcePath = "templates/" + group + "/";
            sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                sourceFile.mkdirs();
            }

            if (checkFiles0()) {
                destinationPath = "temp/" + group + "/" + groupNumber + "/";
                destinationFile = new File(destinationPath);
                if (!destinationFile.exists()) {
                    destinationFile.mkdirs();
                }
                if (copyFiles()) {
                    if (setProperties0()) {
                        logger.info("[" + getId() + "] Service prepared. (" + (System.currentTimeMillis() - startPrepareTime) + "ms)");
                        start();
                    }
                }
            }

        });
        prepareThread.setName(getId().toLowerCase() + "-prepare");
        prepareThread.start();

    }

    private boolean checkFiles0() {
        logger.debug("[" + getId() + "] Checking files...");
        serviceStatus = ServiceStatus.CHECKING_FILES;

        List<String> missingFiles = checkFiles();
        if (!missingFiles.isEmpty()) {
            String out = null;
            for (String missingFile : missingFiles) {
                if (out == null) {
                    out = missingFile;
                } else {
                    out += ", " + missingFile;
                }
            }
            errorRoutine("Canceled preparation! These files are missing: " + out, null);
            return false;
        }

        return true;
    }

    protected abstract List<String> checkFiles();

    private boolean copyFiles() {
        logger.debug("[" + getId() + "] Copying files...");
        serviceStatus = ServiceStatus.COPYING_FILES;

        try {
            FileUtil.copyFolder(sourceFile, destinationFile);
            return true;
        } catch (Exception e) {
            errorRoutine("Could not copy service files!", e);
            return false;
        }
    }

    private boolean setProperties0() {
        logger.debug("[" + getId() + "] Setting properties...");
        serviceStatus = ServiceStatus.SETTING_PROPERTIES;
        return setProperties();
    }

    protected abstract boolean setProperties();

    private boolean start() {
        startStartTime = System.currentTimeMillis();
        serviceStatus = ServiceStatus.STARTING;

        try {

            if (serviceHandler.getOs() == OS.WINDOWS) {
                logger.info("[" + getId() + "] Starting service... (using start.bat)");
                process = new ProcessBuilder().command("cmd", "/c", "start","start.bat").directory(destinationFile).start();
                return true;
            } else if (serviceHandler.getOs() == OS.LINUX) {
                logger.info("[" + getId() + "] Starting service... (using start.sh)");
                process = new ProcessBuilder().command("sh", "-c", "./start.sh").directory(destinationFile).start();
                return true;
            } else {
                errorRoutine("Could not start the service because the OS is unknown!", null);
                return false;
            }

        } catch (Exception e) {
            errorRoutine("Could not start the service!", e);
            return false;
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

        Thread stopThread = new Thread(() -> {

            int exitValue = stop0();
            saveLog();
            delete();

            connection.close();

            serviceHandler.removeService(this);

            logger.info("[" + getId() + "] Service stopped. Exit value: " + exitValue + " (" + (System.currentTimeMillis() - stopStartTime) + "ms)");

        });
        stopThread.setName(getId().toLowerCase() + "-stop");
        stopThread.start();

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

        } catch (Exception e) {
            logger.error("[" + getId() + "] Could not stop the service!", e);
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
        } catch (Exception e) {
            logger.error("[" + getId() + "] Could not save the log!", e);
        }
    }

    private void delete() {
        logger.debug("[" + getId() + "] Deleting service...");
        serviceStatus = ServiceStatus.DELETING;

        try {
            FileUtil.deleteFolder(destinationFile);
        } catch (Exception e) {
            logger.error("[" + getId() + "] Could not delete the service!", e);
        }
    }

    public void setConnection(Connection connection) {
        logger.info("[" + getId() + "] Service started. (" + (System.currentTimeMillis() - startStartTime) + "ms)");
        serviceStatus = ServiceStatus.CONNECTED;
        this.connection = connection;
    }

    public void executeCommand(String command) {
        logger.debug("[" + getId() + "] Executing command: " + command);
        Thread executeCommandThread = new Thread(() -> {

            try {
                OutputStreamWriter out = new OutputStreamWriter(process.getOutputStream());
                out.write(command);
                out.flush();
                out.close();
            } catch (Exception e) {
                logger.error("[" + getId() + "] Could not execute command!", e);
            }

        });
        executeCommandThread.setName(getId().toLowerCase() + "-execute-command");
        executeCommandThread.start();
    }

    protected void errorRoutine(String message, Throwable throwable) {
        logger.error("[" + getId() + "] " + message, throwable);
        if (serviceStatus == ServiceStatus.STARTING || serviceStatus == ServiceStatus.SETTING_PROPERTIES) {
            delete();
        }
        serviceStatus = ServiceStatus.ERROR;
        serviceHandler.removeService(this);
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

    public int getPort() {
        return port;
    }

    public Connection getConnection() {
        return connection;
    }

}
