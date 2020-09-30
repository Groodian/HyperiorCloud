package de.groodian.hyperiorcloud.master.event.events;

import de.groodian.hyperiorcloud.master.event.Event;

public class CommandEnteredEvent implements Event {

    private String command;
    private String[] args;

    public CommandEnteredEvent(String command, String[] args) {
        this.command = command;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

}
