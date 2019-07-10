package service.uml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class UmlService {
    private static Logger logger = LoggerFactory.getLogger(UmlService.class);
    private final String packageName = "input";

    private List<ClassDTO> classDTOs;
    private List<EdgeDTO> edgeDTOs;

    public UmlService(){
        FileService fileService = new FileService();
        Path path = fileService.getPath("/"+packageName);
        List<Class> classes = getClassesForPath(path);
        classDTOs =  classes.stream()
                .map(this::getClassDTOForClass)
                .collect(Collectors.toList());

        edgeDTOs = getEdgeDTOs(classes);
    }

    public List<Class> getClassesForPath(Path path){
        List<String> classNamesAsString = new ArrayList<>();
        List<Class> classes = new ArrayList<>();
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
                classes.add(c);
                logger.info(c.getCanonicalName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    private ClassDTO getClassDTOForClass(Class c){
        List<String> methodsAsString = new ArrayList<>();

        Method[] methods = c.getDeclaredMethods();
        for(Method method : methods) methodsAsString.add(method.getName());

        return new ClassDTO(
                c.isInterface(),
                c.getCanonicalName(),
                c.getSimpleName(),
                (c.getSuperclass() != null) ?  c.getSuperclass().getCanonicalName() : null,
                Arrays.stream(c.getInterfaces()) //implemented interfaces
                        .map(Class::getCanonicalName).collect(Collectors.toList()),
                getFieldsForClass(c),
                getConstructorsForClass(c),
                methodsAsString);
    }

    private List<FieldDTO> getFieldsForClass(Class c){
        List<FieldDTO> fields = new ArrayList<>();

        Field[] declaredFields = c.getDeclaredFields();
        for(Field f : declaredFields){
            String modifier =  Modifier.toString(f.getModifiers()).split(" ")[0];
            modifier = modifier.equals("") ? "package" : modifier;

            fields.add(new FieldDTO(modifier, f.getType().getSimpleName(), f.getName()));
        }
        return fields;
    }

    private List<ConstructorDTO> getConstructorsForClass(Class c){
        List<ConstructorDTO> constructors = new ArrayList<>();

        for (Constructor constructor : c.getDeclaredConstructors()) {
            String modifier =  Modifier.toString(constructor.getModifiers()).split(" ")[0];
            modifier = modifier.equals("") ? "package" : modifier;

            List<String> params = Arrays.stream(constructor.getParameterTypes())
                    .map(Class::getSimpleName).collect(Collectors.toList());

            constructors.add(new ConstructorDTO(modifier,c.getSimpleName(),params));
        }

        return constructors;
    }



    private List<EdgeDTO> getEdgeDTOs(List<Class> classes) {
        List<EdgeDTO> edgeDTOS = new ArrayList<>();

        for(Class clazz : classes) {
            Class superClass = clazz.getSuperclass();
            //Todo: Include Object class
            if(superClass != null && !superClass.getSimpleName().equals("Object"))
                edgeDTOS.add(new EdgeDTO(clazz.getSimpleName(),superClass.getSimpleName(),"extends"));

            List<Class> implementedInterfaces = Arrays.stream(clazz.getInterfaces()).collect(Collectors.toList());
            for (Class implementedInterface : implementedInterfaces)
                edgeDTOS.add(new EdgeDTO(clazz.getSimpleName(),implementedInterface.getSimpleName(),"implements"));
        }
        return edgeDTOS;
    }

    public List<ClassDTO> getClassDTOs() {
        return classDTOs;
    }

    public List<EdgeDTO> getEdgeDTOs() {
        return edgeDTOs;
    }
}
