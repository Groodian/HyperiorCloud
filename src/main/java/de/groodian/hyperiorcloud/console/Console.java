package de.groodian.hyperiorcloud.console;

import de.groodian.hyperiorcloud.command.CommandManager;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;

public class Console {

    private static final String PROMPT = new AttributedStringBuilder()
            .append("HyperiorCloud", ConsoleColor.AQUA.getStyle())
            .append(" >", ConsoleColor.GRAY.getStyle())
            .append(" ", ConsoleColor.WHITE.getStyle())
            .toAnsi();

    private Terminal terminal;
    private LineReader lineReader;
    private Thread thread;
    private CommandManager commandManager;
    private boolean reading;

    public Console() {
        try {
            TerminalBuilder terminalBuilder = TerminalBuilder.builder().system(true);
            Terminal terminal = terminalBuilder.build();
            LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder();
            lineReaderBuilder.terminal(terminal);
            LineReader lineReader = lineReaderBuilder.build();

            this.terminal = terminal;
            this.lineReader = lineReader;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        reading = true;
        thread = new Thread(() -> {
            while (!thread.isInterrupted()) {
                try {
                    if (reading) {
                        String line = lineReader.readLine(PROMPT).trim();
                        if (!line.isEmpty()) {
                            commandManager.callCommand(line);
                        }
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("console");
        thread.start();
    }

    public void printLine(String line) {
        if (line != null && !line.isEmpty()) {
            try {
                lineReader.printAbove(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopReading() {
        reading = false;
    }

    public void close() {
        try {
            thread.interrupt();
            terminal.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

}
