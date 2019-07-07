package service.jshell;

import exceptions.InvalidCodeException;
import input.Fahrzeug;
import input.Item;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
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
        jshell.eval("import "+packageName+".*;");
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
                        getClassForReference(refName)
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

    public String getRefName(SnippetEvent snippetEvent){
        VarSnippet varSnippet = (VarSnippet) snippetEvent.snippet();
        return varSnippet.name();
    }

    public String getRefName(VarSnippet varSnippet){
        return varSnippet.name();
    }

    public String getRefType(SnippetEvent snippetEvent){
        VarSnippet varSnippet = (VarSnippet) snippetEvent.snippet();
        return varSnippet.typeName();
    }

    public String getRefType(VarSnippet varSnippet){
        return varSnippet.typeName();
    }

    public String getClassForReference(String reference){
        String input = reference+".getClass().getSimpleName();";
        SnippetEvent snippetEvent = null;
        try {
            jShellService.evaluateCode(input);
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        return snippetEvent.value().replace("\"","");
    }

    public String getHashcodeForReference(String reference){
        String input = reference+".hashCode();";
        SnippetEvent snippetEvent = null;
        try {
            jShellService.evaluateCode(input);
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        return snippetEvent.value().replace("\"","");
    }

    public String getPackageForReference(String reference){
        String input = reference+".getClass().getPackage();";
        SnippetEvent snippetEvent = null;
        try {
            jShellService.evaluateCode(input);
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            logger.debug("No package name found for reference: " + reference);
            return "InvalidPackageName";
        }


        String packageNameFull = snippetEvent.value().replace("\"","");
        //packageNameFull has format "package mypackagename"
        return packageNameFull.split(" ")[1];
    }

    public void testGetInstancesLocal(){
        Fahrzeug f = new Fahrzeug("tesla", 20);
        Item i = new Fahrzeug("tesla", 20);

        logger.info(f.toString()+" "+ f.getClass().getDeclaredMethods().length);
        logger.info(i.toString()+" "+ i.getClass().getDeclaredMethods().length);

        logger.info(f.toString()+" "+ f.getClass().getMethods().length);
        logger.info(i.toString()+" "+ i.getClass().getMethods().length);


        for(Method m : f.getClass().getDeclaredMethods()){
            logger.info(m.getName());
        }
    }

    /**
     * Reset the JShell by removing all code snippets.
     */
    public void reset() {
        jshell.snippets().forEach(snippet -> jshell.drop(snippet));
        jshell.eval("import "+packageName+".*;");
    }
}
