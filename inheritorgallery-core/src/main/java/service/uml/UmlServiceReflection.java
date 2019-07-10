package service.uml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UmlServiceReflection {
    private static Logger logger = LoggerFactory.getLogger(UmlServiceReflection.class);
    private final String packageName = "input";

    public UmlServiceReflection(){
        FileService fileService = new FileService();
        Path path = fileService.getPath("/"+packageName);
        getClassesForPath(path);

    }

    public List<Class> getClassesForPath(Path path){
        List<String> classNamesAsString = new ArrayList<>();
        List<Class> absoluteClassNames = new ArrayList<>();
        try {
            classNamesAsString = Files.walk(path)
                    .filter(e -> String.valueOf(e.getFileName()).contains(".class"))
                    .map(e -> e.toUri().toString())
                    .map(e -> e.split(packageName+"/")[1])
                    .map(e -> e.replace(".class",""))
                    .map(e -> e.replace("/","."))
                    .map(e -> packageName+"."+e)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String absoluteClassName : classNamesAsString) {
            try {
                Class c = Class.forName(absoluteClassName);
                absoluteClassNames.add(c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return absoluteClassNames;
    }


}
