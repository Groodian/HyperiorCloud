package de.groodian.hyperiorcloud.master.task;

import de.groodian.hyperiorcloud.master.Master;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    private List<Task> tasks;

    private Thread taskThread;

    public TaskHandler() {
        tasks = new ArrayList<>();
    }

    public void start() {
        Master.getInstance().getLogger().info("[TaskHandler] Starting...");
        taskThread = new Thread(() -> {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                taskThread.interrupt();
            }

            while (!taskThread.isInterrupted()) {

                for (Task task : tasks) {
                    if (task.startCondition()) {
                        Master.getInstance().getServiceHandler().startService(task.getServiceType(), task.getGroup());
                    }
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Master.getInstance().getLogger().debug("[TaskHandler] Stopped checking loop.");
                    break;
                }

            }

        });
        taskThread.setName("task-handler");
        taskThread.start();
        Master.getInstance().getLogger().info("[TaskHandler] Started. (First services are started in 5 seconds)");
    }

    public void registerTask(Task task) {
        if (task != null) {
            tasks.add(task);
        } else {
            Master.getInstance().getLogger().error("Could not register task!", new IllegalArgumentException());
        }
    }

    public void unregisterTask(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
        } else {
            Master.getInstance().getLogger().error("Could not remove the task!", new IllegalArgumentException());
        }
    }

    public void stop() {
        if (taskThread != null) {
            taskThread.interrupt();
        }
    }

}
