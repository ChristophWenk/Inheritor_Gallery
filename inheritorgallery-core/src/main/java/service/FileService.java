package service;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService {

    public Path getPath(String fileName) {
        try {
            return Paths.get(getClass().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getPathAsString(Path path) {
        return path.toString();
    }
}
