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


public class JShellReflection {
    private static Logger logger = LoggerFactory.getLogger(JShellReflection.class);
    private final String packageName;

    public JShellReflection(String packageName) {
        this.packageName = packageName;
    }

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

    public String getClassDTOsSerialized() {
        return serialize(getClassDTOs());
    }

    public List<ClassDTO> getClassDTOs() {

        URL jar = null;
        try {
            jar = new URL("file:/F:/Downloads/jarTest2/build/libs/fhnw-1.0-SNAPSHOT.jar");
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
            logger.info(zipEntry.getName());
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

    private List<MethodDTO> getMethodsForClass(Class c) {
        List<MethodDTO> methods = new ArrayList<>();

        //ToDo: sort methods better so that getter and setter are site by side
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

    public String getFieldValuesForReferenceSerialized(Object reference) {
        return serialize(getFieldValuesForReference(reference));
    }

}
