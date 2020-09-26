package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.logging.Logger;
import de.groodian.hyperiorcloud.master.util.FileUtil;

import java.io.File;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Service {

    protected ServiceHandler serviceHandler;
    protected Logger logger = Master.getInstance().getLogger();

    protected String group;
    protected int groupNumber;
    protected int port;
    protected String stopCommand;
    protected List<String> startArguments;

    private DateFormat dateFormat;
    private ServiceConnection serviceConnection;
    private ServiceStatus serviceStatus;
    private Process process;

    protected String sourcePath;
    protected String destinationPath;
    protected File sourceFile;
    protected File destinationFile;

    private long startStartTime;
    private Thread prepareThread;

    public Service(ServiceHandler serviceHandler, String group, int groupNumber, int port, String stopCommand, List<String> startArguments) {
        this.serviceHandler = serviceHandler;
        this.group = group;
        this.groupNumber = groupNumber;
        this.port = port;
        this.stopCommand = stopCommand;
        this.startArguments = startArguments;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd---HH-mm-ss");

        long startPrepareTime = System.currentTimeMillis();
        logger.info("[" + getId() + "] Preparing service...");

        prepareThread = new Thread(() -> {

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
                        if (start()) {
                            startTimeout();
                        }
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
            logger.info("[" + getId() + "] Starting service...");

            String logPath = "logs/" + group + "/";
            File logFile = new File(logPath);
            if (!logFile.exists()) {
                logFile.mkdirs();
            }
            logPath = logPath + dateFormat.format(new Date()) + "---" + groupNumber + ".log";
            logFile = new File(logPath);
            int count = 2;
            while (logFile.exists()) {
                logFile = new File(logPath.replace(".log", "") + "---[#" + count + "].log");
                count++;
            }

            process = new ProcessBuilder().command(startArguments).directory(destinationFile).redirectErrorStream(true).redirectOutput(logFile).start();
            return true;

        } catch (Exception e) {
            errorRoutine("Could not start the service!", e);
            return false;
        }
    }

    private void startTimeout() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            return;
        }

        if (serviceConnection == null) {
            logger.info("[" + getId() + "] The service started 30 seconds ago and has not logged in.");
            stop();
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

            String exitMode;
            int exitValue = stop0();
            switch (exitValue) {
                case 0:
                    exitMode = "normal";
                    break;
                case 1:
                    exitMode = "&cforcibly&r";
                    break;
                case -1:
                default:
                    return;
            }
            delete();

            if (serviceConnection != null) {
                serviceConnection.close();
            }

            serviceHandler.removeService(this);

            logger.info("[" + getId() + "] Service stopped. (" + exitMode + ") (" + (System.currentTimeMillis() - stopStartTime) + "ms)");

        });
        stopThread.setName(getId().toLowerCase() + "-stop");
        stopThread.start();

    }

    private int stop0() {
        try {

            if (process.isAlive()) {
                executeCommand(stopCommand);
                if (process.waitFor(20, TimeUnit.SECONDS)) {
                    return 0;
                }
            }

            process.destroyForcibly();
            process.waitFor();
            return 1;

        } catch (Exception e) {
            logger.error("[" + getId() + "] Could not stop the service!", e);
            return -1;
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

    public void setConnection(ServiceConnection serviceConnection) {
        logger.info("[" + getId() + "] Service started. (" + (System.currentTimeMillis() - startStartTime) + "ms)");
        serviceStatus = ServiceStatus.CONNECTED;
        prepareThread.interrupt();
        this.serviceConnection = serviceConnection;
    }

    public void executeCommand(String command) {
        logger.debug("[" + getId() + "] Executing command: " + command);
        Thread executeCommandThread = new Thread(() -> {

            try {
                OutputStreamWriter out = new OutputStreamWriter(process.getOutputStream());
                out.write(command + "\n");
                out.flush();
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

    public ServiceConnection getConnection() {
        return serviceConnection;
    }

}
