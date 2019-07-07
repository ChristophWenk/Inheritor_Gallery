package service;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class provides tools to read in file paths dynamically
 */
public class FileService {

    /**
     * Create a file path for a given filename
     * @param fileName The file that needs to be referenced
     * @return A path to the given file
     */
    public Path getPath(String fileName) {
        try {
            return Paths.get(getClass().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Convert a path to a String
     * @param path The path to be converted
     * @return The path in String format
     */
    public String getPathAsString(Path path) {
        return path.toString();
    }
}
