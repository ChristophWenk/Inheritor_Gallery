package service.jshell;

import exceptions.InvalidCodeException;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
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
     *
     * @param input The code the JShell should execute
     * @return The output returned by JShell
     */
    public String processInput(String input) {

        if (input.contains("//") || input.contains("/*")) {
            //ToDo: implement cleansing mechanism
            return "Comments are not allowed as input. Remove comments and try again.";
        }
        List<SnippetEvent> snippetEventsList = jshell.eval(input);
        if (snippetEventsList.get(0).status().name().contains("REJECTED")) {
            //Todo: eventually throw exception
            //throw new InvalidCodeException("Code could not be interpreted by JShell. Please verify the statement.");
            return "Could not process input. Please verify the correctness of your statement.";
        }
        else {
            logger.info(""+snippetEventsList);
            return snippetEventsList.get(0).value();

        }
    }


}
