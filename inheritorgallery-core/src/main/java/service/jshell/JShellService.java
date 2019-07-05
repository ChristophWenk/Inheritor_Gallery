package service.jshell;

import exceptions.InvalidCodeException;
import input.Fahrzeug;
import input.Item;
import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Singleton JShellService
 */
public class JShellService {
    private JShell jshell;

    private static Logger logger = LoggerFactory.getLogger(JShellService.class);

    private static JShellService jShellService;


    private JShellService() {
        jshell = JShell.create();
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        for (String cp : classpathEntries)
            jshell.addToClasspath(cp);
        jshell.eval("import input.*;");
    }

    public static JShellService getInstance() {
        if (jShellService == null) {
            jShellService = new JShellService();
        }
        return jShellService;
    }

    /**
     * @param input The code the JShell should execute
     * @return The output returned by JShell
     */
    public String cleanseInput(String input) throws InvalidCodeException {
        if (input.contains("//") || input.contains("/*")) {
            throw new InvalidCodeException("Comments are not allowed.");
        }
        return input;
    }

    public SnippetEvent evaluateCode(String code) throws InvalidCodeException {
        code = cleanseInput(code);

        List<SnippetEvent> snippetEventsList = jshell.eval(code);
        if (!snippetEventsList.get(0).status().name().contains("VALID")) {
            throw new InvalidCodeException("Code could not be interpreted by JShell. Please verify the statement.");
        }
        return snippetEventsList.get(0);
    }

    public String getOutputAsString(SnippetEvent snippetEvent){
        return snippetEvent.value();
    }

    public String getRefName(SnippetEvent snippetEvent){
        VarSnippet varSnippet = (VarSnippet) snippetEvent.snippet();
        return varSnippet.name();
    }

    public String getRefType(SnippetEvent snippetEvent){
        VarSnippet varSnippet = (VarSnippet) snippetEvent.snippet();
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
}
