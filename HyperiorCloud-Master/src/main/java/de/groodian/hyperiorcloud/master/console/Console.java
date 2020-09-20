package de.groodian.hyperiorcloud.master.console;

import de.groodian.hyperiorcloud.master.command.CommandManager;
import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;

public class Console {

    private ConsoleReader reader;
    private Thread thread;
    private CommandManager commandManager;

    public Console() {
        AnsiConsole.systemInstall();

        try {
            reader = new ConsoleReader();
            reader.setPrompt(ConsoleColor.translateColorCodes("&bHyperiorCloud &7> &f"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        startReading();
    }

    private void startReading() {
        thread = new Thread(() -> {
            while (!thread.isInterrupted()) {
                try {
                    reader.getOutput().write("\u001b[1G\u001b[K");
                    reader.flush();
                    processLine(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("console");
        thread.start();
    }

    private void processLine(String line) {
        if (line != null && !line.isEmpty()) {
            commandManager.callCommand(line);
        }
    }

    public void printLine(String line) {
        if (line != null && !line.isEmpty()) {

            try {

                CursorBuffer cursorBuffer = reader.getCursorBuffer().copy();
                reader.getOutput().write("\u001b[1G\u001b[K");
                reader.flush();
                reader.getOutput().write(line);
                reader.resetPromptLine(reader.getPrompt(), cursorBuffer.toString(), cursorBuffer.cursor);
                reader.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void clearScreen() {
        try {
            reader.clearScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        thread.interrupt();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

}
