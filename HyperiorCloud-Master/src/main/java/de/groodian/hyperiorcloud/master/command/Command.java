package de.groodian.hyperiorcloud.master.command;

public abstract class Command {

    protected String[] name;
    protected String description;

    public Command(String description, String... name) {
        this.description = description;
        this.name = name;
    }

    public abstract void execute(String[] args);

    public String[] getNames() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
