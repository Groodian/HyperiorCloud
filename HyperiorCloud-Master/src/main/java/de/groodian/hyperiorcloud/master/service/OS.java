package de.groodian.hyperiorcloud.master.service;

import de.groodian.hyperiorcloud.master.Master;

public enum OS {

    WINDOWS,
    LINUX,
    UNKNOWN;

    public static OS getOS() {
        Master.getInstance().getLogger().info("Scanning OS...");
        String osName = System.getProperty("os.name").toLowerCase();
        Master.getInstance().getLogger().info("OS Name: " + System.getProperty("os.name"));
        if (osName.contains("windows")) {
            Master.getInstance().getLogger().info("OS: Windows");
            return OS.WINDOWS;
        } else if (osName.contains("linux")) {
            Master.getInstance().getLogger().info("OS: Linux");
            return OS.LINUX;
        } else {
            Master.getInstance().getLogger().info("OS: Unknown");
            return OS.UNKNOWN;
        }
    }

}
