package service.jshell;

import exceptions.InvalidCodeException;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;
import jshellExtensions.JShellReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;
import service.jshell.dto.FieldDTO;
import service.jshell.dto.ObjectDTO;
import service.jshell.dto.ReferenceDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton JShellService.
 * Provides all tools necessary to interact with the JShell API.
 * There can only be one JShellService instance in the whole application.
 * JShell manages all the instances created by the user internally.
 */
public class JShellService {
    private static Logger logger = LoggerFactory.getLogger(JShellService.class);

    private static JShellService jShellService;
    private JShell jshell;
    private final String packageName = "input";
    private final String packageNameJShellReflection = "jshellExtensions";

    /**
     * JShellService Constructor.
     */
    private JShellService() {
        jshell = JShell.create();

        // The JVM classpath needs to be explicitly added to JShell so that it is able to use non-JDK classes.
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        for (String cp : classpathEntries)
            jshell.addToClasspath(cp);

        // Classes need to be explicitly imported to JShell similarly as if we wanted to import one into a class.
        //jshell.eval("import "+packageName+".*;");
        //jshell.eval("import java.lang.reflect.Field;");
        importPackage(packageName);
        importPackage(packageNameJShellReflection);

        jshell.eval("JShellReflection jshellReflection = new JShellReflection();");
        //JShellReflection jShellReflection = new JShellReflection();
    }

    private void importPackage(String packageName){
        FileService fileService = new FileService();
        Path path = fileService.getPath("/"+packageName);
        try {
            Files.walk(path)
                    .map(e -> new File(e.toString()))
                    .filter(File::isDirectory)
                    .map(e -> e.toURI().toString().split(packageName))
                    .map(e -> packageName+e[1].replace("/","."))
                    .map(e -> "import "+e+"*;")
                    .forEach(e -> jshell.eval(e));
        } catch (IOException e) {
            e.printStackTrace();
        }

        jshell.eval("import java.lang.reflect.Field;");
    }

    /**
     * Singleton pattern.
     * @return A Singleton JShellService instance.
     */
    public static JShellService getInstance() {
        if (jShellService == null) {
            jShellService = new JShellService();
        }
        return jShellService;
    }

    /**
     * Catch invalid code inputs given by a user.
     * @param code The code given by the user.
     * @return The cleansed code.
     * @throws InvalidCodeException
     */
    public String cleanseInput(String code) throws InvalidCodeException {
        if (code.contains("//") || code.contains("/*")) {
            logger.error("User entered comment: " + code);
            throw new InvalidCodeException("Comments are not allowed.");
        }
        return code;
    }

    /**
     * @param code The code the JShell should execute.
     * @return The output returned by JShell.
     */
    public SnippetEvent evaluateCode(String code) throws InvalidCodeException {
        code = cleanseInput(code);

        List<SnippetEvent> snippetEventsList = jshell.eval(code);
        if (!snippetEventsList.get(0).status().name().contains("VALID")) {
            logger.error("User code could not be interpreted by JShell: " + code);
            throw new InvalidCodeException("Code could not be interpreted by JShell. Please verify the statement.");
        }
        //ToDo: When redeclaring an instance multiple snippets are created. Add Error Handling
        return snippetEventsList.get(0);
    }

    public List<ObjectDTO> getObjectDTOs(){
        List<ObjectDTO> objectDTOList = new ArrayList<>();

        String refName;

        List<VarSnippet> variablesList = jshell.variables().collect(Collectors.toList());

        for(VarSnippet varSnippet : variablesList ){
            refName = getRefName(varSnippet);

            if(varSnippet.subKind().toString().contains("VAR_DECLARATION")
                    && getPackageForReference(refName).equals(packageName)){
                ObjectDTO objectDTO = new ObjectDTO(
                        getHashcodeForReference(refName),
                        getClassForReference(refName),
                        getFieldsForReference(refName)
                );
                // Check for an existing item in the list
                if(objectDTOList.stream()
                .noneMatch(o -> o.getObjectId().equals(objectDTO.getObjectId())))
                {  objectDTOList.add(objectDTO); }

            }
        }
        return objectDTOList;
    }

    public List<ReferenceDTO> getReferenceDTOs(){
        List<ReferenceDTO> referenceDTOsList = new ArrayList<>();
        String refName;

        List<VarSnippet> variablesList = jshell.variables().collect(Collectors.toList());

        for(VarSnippet varSnippet : variablesList ){
            refName = getRefName(varSnippet);

            if(varSnippet.subKind().toString().contains("VAR_DECLARATION")
                    && getPackageForReference(refName).equals(packageName)){
                ReferenceDTO referenceDTO = new ReferenceDTO(
                        getRefType(varSnippet),
                        refName,
                        getHashcodeForReference(refName)
                );
                referenceDTOsList.add(referenceDTO); }
        }
        return referenceDTOsList;
    }

    public String getOutputAsString(SnippetEvent snippetEvent){
        return snippetEvent.value();
    }

    public String getRefName(VarSnippet varSnippet){
        return varSnippet.name();
    }

    public String getRefType(VarSnippet varSnippet){
        return varSnippet.typeName();
    }

    public String getClassForReference(String reference){
        String input = reference+".getClass().getCanonicalName();";
        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            logger.error("There was a problem retrieving the class for reference: " + reference, e);
        }

        return snippetEvent.value().replace("\"","");
    }

    public String getHashcodeForReference(String reference){
        String input = reference+".hashCode();";
        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            logger.error("There was a problem retrieving the hash code for reference: " + reference, e);
        }
        return snippetEvent.value().replace("\"","");
    }

    public String getPackageForReference(String reference){
        String input = reference+".getClass().getPackage();";
        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            logger.debug("No package name found for reference: " + reference + ". It might be a primitive type.", e);
            return "InvalidPackageName";
        }

        String packageNameFull = snippetEvent.value().replace("\"","");
        //packageNameFull has format "package mypackagename"
        return packageNameFull.split(" ")[1];
    }

    public List<FieldDTO> getFieldsForReference(String reference){
        String input1 = "String fieldDTOsString = \"\";";
        String input2 = "        Class<?> declaringClass  = "+reference+".getClass();";
        String input3 = " while(declaringClass.getSuperclass() != null) {\n" +
                "            for (Field currentField_xyghw : declaringClass.getDeclaredFields()) {\n" +
                "                try {" +
                "                    currentField_xyghw.setAccessible(true);" +
                "                    fieldDTOsString += \";;\"+declaringClass.getCanonicalName()+\";\"" +
                "                                       +currentField_xyghw.getName()+\";\"" +
                "                                       +currentField_xyghw.get("+reference+").toString();\n" +
                "                } catch (IllegalAccessException e) {\n" +
                "                    e.printStackTrace();\n" +
                "                }\n" +
                "            }\n" +
                "            declaringClass = declaringClass.getSuperclass();\n" +
                "        }";
        String input4 = "fieldDTOsString;";

        SnippetEvent snippetEvent = null;
        try {
            jShellService.evaluateCode(input1);
            jShellService.evaluateCode(input2);
            jShellService.evaluateCode(input3);
            snippetEvent = jShellService.evaluateCode(input4);
        } catch (InvalidCodeException e) {
            logger.debug("No methods found reference: " + reference + ". It might be a primitive type.", e);
        }

        List<FieldDTO> fieldDTOs = new ArrayList<>();
        /*
          String formatting source:
          ";;classField1;nameField1;valueField1;;classField2;nameField2;valueField2"
          Target:
          String [classField1;nameField1;valueField1,   classField2;nameField2;valueField2]
        */

        if(snippetEvent.value().length() > 2){
            //logger.info(snippetEvent.value());
            String[] fieldDTOsAsString = snippetEvent.value().replace("\"","").substring(2).split(";;");
            for(String fieldAsString : fieldDTOsAsString){
                String[] fieldAsArray = fieldAsString.split(";");
                fieldDTOs.add(new FieldDTO(fieldAsArray[0],null,null,fieldAsArray[1],fieldAsArray[2]));
            }
        }

        return fieldDTOs;
    }

    /**
     * Reset the JShell by removing all code snippets.
     */
    public void reset() {
        jshell.snippets().forEach(snippet -> jshell.drop(snippet));
        //jshell.eval("import "+packageName+".*;");
        //jshell.eval("import java.lang.reflect.Field;");
        importPackage(packageName);
        importPackage(packageNameJShellReflection);

        jshell.eval("JShellReflection jshellReflection = new JShellReflection();");
    }
}
