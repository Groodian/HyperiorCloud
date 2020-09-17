package de.groodian.hyperiorcloud.master.logging.handler;

import de.groodian.hyperiorcloud.master.Main;
import de.groodian.hyperiorcloud.master.Master;
import de.groodian.hyperiorcloud.master.logging.LogEntry;
import de.groodian.hyperiorcloud.master.logging.LogHandler;
import de.groodian.hyperiorcloud.master.logging.LogLevel;
import de.groodian.hyperiorcloud.master.logging.ILogEntryFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogHandler extends LogHandler {

    private static final long MAX_FILE_SIZE = 1048576L; // 1MB
    private static final String PATH_NAME = "logs/master/";
    private static final String FILE_ENDING = ".log";

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd---HH-mm-ss");
    private boolean maxFileSizeReached = false;
    private File file;
    private PrintWriter printWriter;

    public FileLogHandler(ILogEntryFormatter logEntryFormatter, LogLevel logLevel) {
        super(logEntryFormatter, logLevel);

        try {
            file = new File(PATH_NAME);
            file.mkdirs();

            file = new File(PATH_NAME + dateFormat.format(new Date()) + FILE_ENDING);
            int count = 2;
            while (file.exists()) {
                file = new File(PATH_NAME + dateFormat.format(new Date()) + "---[#" + count + "]" + FILE_ENDING);
                count++;
            }
            file.createNewFile();

            printWriter = new PrintWriter(new FileWriter(file));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handle(LogEntry logEntry) {
        if(!maxFileSizeReached) {
            if (file.length() < MAX_FILE_SIZE) {
                printWriter.write(logEntryFormatter.format(logEntry));
            } else {
                maxFileSizeReached = true;
                LogEntry errorLogEntry = new LogEntry(LogLevel.FATAL, System.currentTimeMillis(), "Max file size of " + MAX_FILE_SIZE + " bytes for file " + file.toString() + " reached!", null, Thread.currentThread(), this.getClass());
                printWriter.write(logEntryFormatter.format(errorLogEntry));
                Master.getInstance().getLogger().log(errorLogEntry);
            }

            printWriter.flush();
        }

    }

    @Override
    public void close() {
        printWriter.flush();
        printWriter.close();
    }

}
