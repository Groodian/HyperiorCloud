package de.groodian.hyperiorcloud.master.task;

public abstract class Task {

    private String group;

    public Task(String group) {
        this.group = group;
    }

    protected abstract boolean startCondition();

    public String getGroup() {
        return group;
    }

}
