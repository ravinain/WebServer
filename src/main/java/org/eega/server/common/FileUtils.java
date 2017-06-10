package org.eega.server.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Ravi Nain on 6/10/2017.
 * Common File utilities,
 */
public final class FileUtils {

    private FileUtils(){}

    /**
     * Checks whether input file is regular file(neither directory nor shortcut link) or not.
     * @param fileName Input file path.
     * @return TRUE|FALSE
     */
    public static boolean isRegularFile(final String fileName) {
        return Files.isRegularFile(Paths.get(fileName));
    }

    /**
     * Checks whether input file exists or not.
     * @param fileName Input file path.
     * @return TRUE|FALSE
     */
    public static boolean isFileExists(final String fileName) {
        return Paths.get(fileName).toFile().exists();
    }
}
