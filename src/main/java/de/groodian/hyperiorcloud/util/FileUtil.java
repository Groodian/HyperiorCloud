package de.groodian.hyperiorcloud.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    public static void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
        // Check if sourceFolder is a directory or file
        // If sourceFolder is file; then copy the file directly to new location
        if (sourceFolder.isDirectory()) {
            // Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
            }

            // Get all files from source directory
            String files[] = sourceFolder.list();

            // Iterate over all files and copy them to destinationFolder one by one
            for (String file : files) {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);

                // Recursive function call
                copyFolder(srcFile, destFile);
            }
        } else {
            // Copy the file content from one place to another
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void deleteFolder(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteFolder(file);
            }
        }
        directoryToBeDeleted.delete();
    }

}
