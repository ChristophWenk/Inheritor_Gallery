package jshellExtensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.jshell.dto.ClassDTO;
import service.jshell.dto.ConstructorDTO;
import service.jshell.dto.FieldDTO;
import service.jshell.dto.MethodDTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class JShellReflection {
    private static Logger logger = LoggerFactory.getLogger(JShellReflection.class);
    private final String packageName;

    public JShellReflection(String packageName){
        this.packageName = packageName;
    }

    public String serialize(Object objectToSerialize){
        String serializedString = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(byteArrayOutputStream);
            o.writeObject(objectToSerialize);
            o.flush();
            o.close();
            serializedString =  Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializedString;
    }

    public String getClassDTOsSerialized(){
        return serialize(getClassDTOs());
    }

    public List<ClassDTO> getClassDTOs() {
        logger.info(packageName);


//        CodeSource src = JShellReflection.class.getProtectionDomain().getCodeSource();
//        logger.info(src.getLocation().toExternalForm());
        //if (src != null) {
            //URL jar = src.getLocation();
        URL jar = null;
        try {
            jar = new URL("file:/F:/Downloads/jarTest2/build/libs/fhnw-1.0-SNAPSHOT.jar");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        logger.info(jar.toExternalForm());
            ZipInputStream zip = null;
            try {
                zip = new ZipInputStream(jar.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true) {
                ZipEntry e = null;
                try {
                    e = zip.getNextEntry();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (e == null) {
                    break;
                }

                String name = e.getName();
                if (name.startsWith("b")) {
                    /* Do something with this entry. */
                logger.info("Name of Content: " + e.getName());
                }
            }
//        }
//        else {
//            /* Fail... */
//        }





        URL input = getClass().getResource("/b");
        logger.info("URL: " + input);
        File path = null;
        path =new File(input.toExternalForm());

        try {
            logger.info("URI: " + input.toURI().toString());
            Class c = Class.forName("b.B");
            logger.info("ClassName: " + c.getName());
            logger.info("Path: " + input.getPath());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        logger.info(path.toString());

//        Path path = fileService.getPath("/"+packageName);
//        logger.info(path.toPath().toString());

        List<Class> classes = getClassesForPath(path.toPath());
        return  classes.stream()
                .map(this::getClassDTOForClass)
                .collect(Collectors.toList());

    }

    public List<Class> getClassesForPath(Path path){
        logger.info("Path: " + path.toString());
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
                logger.info(absoluteClassName);
                classes.add(c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    private ClassDTO getClassDTOForClass(Class c){

        return new ClassDTO(
                Modifier.isAbstract(c.getModifiers()),
                c.isInterface(),
                c.getCanonicalName(),
                c.getSimpleName(),
                (c.getSuperclass() != null) ?  c.getSuperclass().getCanonicalName() : null,
                Arrays.stream(c.getInterfaces()) //implemented interfaces
                        .map(Class::getCanonicalName).collect(Collectors.toList()),
                getFieldsForClass(c),
                getConstructorsForClass(c),
                getMethodsForClass(c));
    }

    private List<FieldDTO> getFieldsForClass(Class c){
        List<FieldDTO> fields = new ArrayList<>();

        Field[] declaredFields = c.getDeclaredFields();
        for(Field f : declaredFields){
            String modifier =  Modifier.toString(f.getModifiers()).split(" ")[0];
            modifier = modifier.equals("") ? "package" : modifier;

            fields.add(new FieldDTO(
                                    c.getCanonicalName(),
                                    modifier,
                                    f.getType().getSimpleName(),
                                    f.getName(),
                                    null));
        }
        return fields;
    }

    private List<ConstructorDTO> getConstructorsForClass(Class c){
        List<ConstructorDTO> constructors = new ArrayList<>();

        Stream<Constructor> constructorList = Arrays.stream(c.getDeclaredConstructors())
                .sorted(Comparator.comparing(Constructor::getParameterCount));

        constructorList.forEach(constructor -> {
            String modifier =  Modifier.toString(constructor.getModifiers()).split(" ")[0];
            modifier = modifier.equals("") ? "package" : modifier;


            List<String> params = Arrays.stream(constructor.getParameterTypes())
                    .map(Class::getSimpleName).collect(Collectors.toList());
            constructors.add(new ConstructorDTO(modifier,c.getSimpleName(),params));
                }
        );
        return constructors;
    }

    private List<MethodDTO> getMethodsForClass(Class c){
        List<MethodDTO> methods = new ArrayList<>();

        //ToDo: sort methods better so that getter and setter are site by side
        Stream<Method> methodList = Arrays.stream(c.getDeclaredMethods())
                .sorted(Comparator.comparing(m -> m.getParameterTypes().length))
                .sorted(Comparator.comparing(Method::getName));


        methodList.forEach(method -> {
            String modifier =  Modifier.toString(method.getModifiers()).split(" ")[0];
            modifier = modifier.equals("") ? "package" : modifier;

            List<String> params = Arrays.stream(method.getParameterTypes())
                    .map(Class::getSimpleName).collect(Collectors.toList());

            methods.add(new MethodDTO(
                    modifier,
                    method.getReturnType().getSimpleName(),
                    method.getName(),
                    params
            ));
        });

        return methods;
    }

    public List<FieldDTO> getFieldValuesForReference(Object reference) {

        List<FieldDTO> fieldDTOs = new ArrayList<>();
        Class<?> declaringClass = reference.getClass();
        while (declaringClass.getSuperclass() != null) {
            for (Field currentField_xyghw : declaringClass.getDeclaredFields()) {
                try {
                      currentField_xyghw.setAccessible(true);
                      String value;
                      if(currentField_xyghw.get(reference) == null) value = "null";
                      else value = currentField_xyghw.get(reference).toString();

                      fieldDTOs.add(new FieldDTO(
                               declaringClass.getCanonicalName(),
                               null,
                               currentField_xyghw.getType().getSimpleName(),
                               currentField_xyghw.getName(),
                               value
                      ));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            declaringClass = declaringClass.getSuperclass();
        }
        return fieldDTOs;
    }

    public String  getFieldValuesForReferenceSerialized(Object reference){
        return serialize(getFieldValuesForReference(reference));
    }

}
