package de.groodian.hyperiorcloud.master.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LogOutputStream extends ByteArrayOutputStream {

    private Logger logger;
    private LogLevel logLevel;

    public LogOutputStream(Logger logger, LogLevel logLevel) {
        this.logger = logger;
        this.logLevel = logLevel;
    }

    @Override
    public void flush() {
        String output = toString();
        reset();

        if (output != null && !output.isEmpty() && !output.equals(System.lineSeparator())) {
            logger.log(logLevel, output);
        }

    }

    public Logger getLogger() {
        return logger;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
