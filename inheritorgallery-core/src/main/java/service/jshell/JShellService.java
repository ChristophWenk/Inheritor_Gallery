package service.jshell;

import exceptions.InvalidCodeException;
import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.jshell.dto.ClassDTO;
import service.jshell.dto.FieldDTO;
import service.jshell.dto.ObjectDTO;
import service.jshell.dto.ReferenceDTO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    private String packageName, jarPath;

    /**
     * JShellService Constructor.
     */
    private JShellService() {
        jshell = JShell.create();

        // The JVM classpath needs to be explicitly added to JShell so that it is able to use non-JDK classes.
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        for (String cp : classpathEntries) {
            jshell.addToClasspath(cp);
        }
    }

    public void updateImports(Path pathAsObject){
        String path;

        if(File.separator.equals("\\")){
            logger.debug("path separator"+ File.separator);
            path = pathAsObject.toUri().toString().replace("file:///","file:/");
        }
        else {
            logger.debug("path separator"+ File.separator);
            path = pathAsObject.toUri().toString();
        }

        logger.info("path " + path);
        setJarPath(path);
        logger.debug("updateImports: " + getJarPath());
        String packageNameFromJar = getPackageFromJar(path);

        String pathWithoutFile = path.replace("file:/","");
        pathWithoutFile = pathWithoutFile.replace("file:///","");
        logger.debug("addToClasspath " + pathWithoutFile);
        jshell.addToClasspath(pathWithoutFile);

        setPackageName(packageNameFromJar);
        importClasses();
    }



    private void importClasses(){
        // Classes need to be explicitly imported to JShell similarly as if we wanted to import one into a class.
        logger.info("importing " + getPackageName());
        jshell.eval("import "+getPackageName()+".*;");
        jshell.eval("import jshellExtensions.*;");
        jshell.eval("JShellReflection jshellReflection = new JShellReflection(\""+getPackageName()+"\");");
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
        if (!code.contains("file://") && (code.contains("//") || code.contains("/*"))) {
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
        return snippetEventsList.get(0);
    }

    public List<ClassDTO> getClassDTOs(){
        List<ClassDTO> classDTOs = new ArrayList<>();
        if(getJarPath() != null){
            SnippetEvent snippetEvent = null;
            try {
                snippetEvent = jShellService.evaluateCode("jshellReflection.getClassDTOsSerialized(\""+getJarPath()+"\");");
            } catch (InvalidCodeException e) {
                logger.error("There was a problem while retrieving ClassDTOs from JShell.", e);
            }

            //snippetEvent.value() return the serialized String with ""
            try {
                String serializedString = snippetEvent.value().substring(1, snippetEvent.value().length() - 1);
                String classDTOsSerialized = serializedString;


                // deserialize the object
                try {
                    byte[] data = Base64.getDecoder().decode(classDTOsSerialized);
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                    classDTOs = (List<ClassDTO>) ois.readObject();
                } catch (Exception e) {
                    logger.error("Could not deserialize object: " + classDTOsSerialized, e);
                }
            } catch (NullPointerException e) {
                logger.error("Could not serialize ClassDTOs");
            }
        }

        return classDTOs;
    }

    public List<ObjectDTO> getObjectDTOs(){
        List<ObjectDTO> objectDTOList = new ArrayList<>();

        String refName;

        List<VarSnippet> variablesList = jshell.variables().collect(Collectors.toList());
        for(VarSnippet varSnippet : variablesList ){
            refName = getRefName(varSnippet);

            if(varSnippet.subKind().toString().contains("VAR_DECLARATION")
                    && getPackageForReference(refName).equals(getPackageName())){
                ObjectDTO objectDTO = new ObjectDTO(
                        getHashcodeForReference(refName),
                        getClassForReference(refName),
                        getFieldValuesReference(refName)
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
                    && getPackageForReference(refName).equals(getPackageName())){
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
            logger.error("No package name found for reference: " + reference + ". It might be a primitive type.", e);
            return "InvalidPackageName";
        }

        String packageNameFull = snippetEvent.value().replace("\"","");
        //packageNameFull has format "package mypackagename"
        return packageNameFull.split(" ")[1];
    }

    public List<FieldDTO> getFieldValuesReference(String reference){
        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode("jshellReflection.getFieldValuesForReferenceSerialized("+reference+");");
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        String serializedString = snippetEvent.value().substring(1,snippetEvent.value().length()-1);
        String fieldDTOsSerialized = serializedString;

        List<FieldDTO> fieldDTOs = new ArrayList<>();

        // deserialize the object
        try {
            byte [] data = Base64.getDecoder().decode( fieldDTOsSerialized );
            ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream( data ) );
            fieldDTOs  = (List<FieldDTO>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fieldDTOs;

    }

    /**
     * Reset the JShell by removing all code snippets.
     */
    public void reset() {
        jshell.snippets().forEach(snippet -> jshell.drop(snippet));
        importClasses();
    }

    /**
     * Check if there exists a reference declaration that needs to be deleted.
     */
    public void checkforDeletion() {
        String isNullPattern = ".*=.*null;";
        String deleteRefName = "";
        String refName = "";
        Snippet nullSnippet = null;

        List<Snippet> snippetList = jshell.snippets().collect(Collectors.toList());
        for (Snippet snippet : snippetList) {
            if (Pattern.matches(isNullPattern,snippet.source()) && jshell.status(snippet).name().equals("VALID")) {
                String parts[] = snippet.source().split("=");
                deleteRefName = parts[0].replace(" ","");
                nullSnippet = snippet;
            }
        }

        List<VarSnippet> variablesList = jshell.variables().collect(Collectors.toList());
        for(VarSnippet varSnippet : variablesList ) {
            refName = getRefName(varSnippet);
            if (refName.equals(deleteRefName)) {
                jshell.drop(varSnippet);
                jshell.drop(nullSnippet);
            }
        }
    }

    private String getPackageFromJar(String jarPath) {
        logger.debug("getPackageFromJar: " + jarPath);
        URL jar = null;
        try {
            jar = new URL(jarPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ZipInputStream zip = null;
        try {
            zip = new ZipInputStream(jar.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
//            logger.debug(zipEntry.getName());
            if (zipEntry.getName().endsWith("/") && !zipEntry.getName().equals("META-INF/")) {
//                logger.debug("Name of Content: " + zipEntry.getName());

                return zipEntry.getName().replace("/","");
            }
        }
        return null;
    }


    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }
}
