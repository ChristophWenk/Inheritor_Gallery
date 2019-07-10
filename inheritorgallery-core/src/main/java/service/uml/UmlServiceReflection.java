package service.uml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UmlServiceReflection {
    private static Logger logger = LoggerFactory.getLogger(UmlServiceReflection.class);

    public UmlServiceReflection(){
        FileService fileService = new FileService();
        Path path = fileService.getPath("/input");

        try {
            Files.walk(path).forEach(e -> logger.info(e.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
