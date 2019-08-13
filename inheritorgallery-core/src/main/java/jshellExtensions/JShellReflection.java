package jshellExtensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.jshell.dto.ClassDTO;
import service.jshell.dto.ConstructorDTO;
import service.jshell.dto.FieldDTO;
import service.jshell.dto.MethodDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class provides reflection information like a classes methods and fields.
 * It is meant to be executed within JShell.
 */
public class JShellReflection {
    private static Logger logger = LoggerFactory.getLogger(JShellReflection.class);
    private final String packageName;

    /**
     * Create the JShellReflection
     * @param packageName The package name of the classes to be analyzed
     */
    public JShellReflection(String packageName) {
        logger.debug("JShellReflection created");
        this.packageName = packageName;
    }

    /**
     * Serialize an object
     * @param objectToSerialize
     * @return String of the serialized object
     */
    public String serialize(Object objectToSerialize) {
        String serializedString = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(byteArrayOutputStream);
            o.writeObject(objectToSerialize);
            o.flush();
            o.close();
            serializedString = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializedString;
    }

    /**
     * Get all ClassDTOs in serialized form
     * @param pathToJar Path to the .jar holding the classes
     * @return String of the serialized ClassDTOs
     */
    public String getClassDTOsSerialized(String pathToJar) {
        return serialize(getClassDTOs(pathToJar));
    }

    /**
     * Get a List of ClassDTOs created by classes belonging to a specific .jar
     * @param pathToJar Path to the .jar holding the classes
     * @return List of ClassDTOs
     */
    public List<ClassDTO> getClassDTOs(String pathToJar) {
        logger.info("Reflection 1 " + pathToJar);
        URL jar = null;
        try {
            jar = new URL(pathToJar);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ZipInputStream zip = null;
        try {
            zip = new ZipInputStream(jar.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> classNamesAsString = new ArrayList<>();
        List<Class> classes = new ArrayList<>();
        while (true) {
            ZipEntry zipEntry = null;
            try {
                zipEntry = zip.getNextEntry();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (zipEntry == null) {
                break;
            }
            //logger.debug("Entry: " + zipEntry.getName());
            if (zipEntry.getName().endsWith(".class")) {
                classNamesAsString.add(
                        zipEntry.getName()
                                .replace("/", ".")
                                .replace(".class", "")
                );
                logger.debug("Name of Content: " + zipEntry.getName());
            }
        }

        for (String absoluteClassName : classNamesAsString) {
            try {
                Class c = Class.forName(absoluteClassName);
                classes.add(c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classes.stream()
                .map(this::getClassDTOForClass)
                .collect(Collectors.toList());

    }

    /**
     * Create a classDTOFor
     * @param c The class for which a ClassDTO should be created
     * @return a ClassDTO holding information like methods and fields
     */
    private ClassDTO getClassDTOForClass(Class c) {
        return new ClassDTO(
                Modifier.isAbstract(c.getModifiers()),
                c.isInterface(),
                c.getCanonicalName(),
                c.getSimpleName(),
                (c.getSuperclass() != null) ? c.getSuperclass().getCanonicalName() : null,
                Arrays.stream(c.getInterfaces()) //implemented interfaces
                        .map(Class::getCanonicalName).collect(Collectors.toList()),
                getFieldsForClass(c),
                getConstructorsForClass(c),
                getMethodsForClass(c));
    }

    /**
     * Get all fields for a class
     * @param c The class for which the fields should be analyzed
     * @return List of FieldsDTOs holding information like modifier and name
     */
    private List<FieldDTO> getFieldsForClass(Class c) {
        List<FieldDTO> fields = new ArrayList<>();

        Field[] declaredFields = c.getDeclaredFields();
        for (Field f : declaredFields) {
            String modifier = Modifier.toString(f.getModifiers()).split(" ")[0];
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

    /**
     * Get all constructors for a class
     * @param c The class for which the constructors should be analyzed
     * @return List of ConstructorDTOs holding information like modifier and name
     */
    private List<ConstructorDTO> getConstructorsForClass(Class c) {
        List<ConstructorDTO> constructors = new ArrayList<>();

        Stream<Constructor> constructorList = Arrays.stream(c.getDeclaredConstructors())
                .sorted(Comparator.comparing(Constructor::getParameterCount));

        constructorList.forEach(constructor -> {
                    String modifier = Modifier.toString(constructor.getModifiers()).split(" ")[0];
                    modifier = modifier.equals("") ? "package" : modifier;


                    List<String> params = Arrays.stream(constructor.getParameterTypes())
                            .map(Class::getSimpleName).collect(Collectors.toList());
                    constructors.add(new ConstructorDTO(modifier, c.getSimpleName(), params));
                }
        );
        return constructors;
    }

    /**
     * Get all methods for a class
     * @param c The class for which the methods should be analyzed
     * @return List of MethodDTOs holding information like modifier and name
     */
    private List<MethodDTO> getMethodsForClass(Class c) {
        List<MethodDTO> methods = new ArrayList<>();

        Stream<Method> methodList = Arrays.stream(c.getDeclaredMethods())
                .sorted(Comparator.comparing(m -> m.getParameterTypes().length))
                .sorted(Comparator.comparing(Method::getName));


        methodList.forEach(method -> {
            String modifier = Modifier.toString(method.getModifiers()).split(" ")[0];
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

    /**
     * Get the currently set values of all fields for a reference
     * @param reference The reference for which the fields should be analyzed
     * @return List of FieldsDTOs including the field values
     */
    public List<FieldDTO> getFieldValuesForReference(Object reference) {

        List<FieldDTO> fieldDTOs = new ArrayList<>();
        Class<?> declaringClass = reference.getClass();
        while (declaringClass.getSuperclass() != null) {
            for (Field currentField_xyghw : declaringClass.getDeclaredFields()) {
                try {
                    currentField_xyghw.setAccessible(true);
                    String value;
                    if (currentField_xyghw.get(reference) == null) value = "null";
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

    /**
     * Get the currently set values of all fields for a reference in serialized form
     * @param reference The reference for which the fields should be analyzed
     * @return String of the serialized field values
     */
    public String getFieldValuesForReferenceSerialized(Object reference) {
        return serialize(getFieldValuesForReference(reference));
    }

}
