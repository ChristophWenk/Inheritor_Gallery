package service;

import exceptions.InvalidCodeException;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Singleton JShellService
 */
//TODO Remove all unused code when finished
public class JShellService {
    private JShell jshell;

    private static Logger logger = LoggerFactory.getLogger(JShellService.class);

    private static JShellService jShellService;

    private JShellService() {
        jshell = JShell.create();
        jshell.addToClasspath("inheritorgallery-core/src/main/resources/classLibrary");
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
     * @param code The code the JShell should execute
     * @return The output returned by JShell
     */
    public String evaluateCode(String code) throws InvalidCodeException {
        List<SnippetEvent> snippetEventsList = jshell.eval(code);
        if (snippetEventsList.get(0).status().name().contains("REJECTED")) {
            throw new InvalidCodeException("Code could not be interpreted by JShell. Please verify the statement.");
        }
        String value = snippetEventsList.get(0).value();
        return value;
    }

    public List<SnippetEvent> getSnippetEventsList(String code) throws InvalidCodeException {
        List<SnippetEvent> snippetEventsList = jshell.eval(code);
        if (snippetEventsList.get(0).status().name().contains("REJECTED")) {
            throw new InvalidCodeException("Code could not be interpreted by JShell. Please verify the statement.");
        }
        return snippetEventsList;
    }


    public String processInput(String jShellCommand) {
        String input = jShellCommand;
        String output = "";
        if (input.contains("//") || input.contains("/*")) {
            logger.info("Comment received. Asking user for new input.");
            return output = ("Comments are not allowed as input. Remove comments and try again.");
        }

        List<SnippetEvent> snippetEventsList = jshell.eval(input);
        if (snippetEventsList.get(0).status().name().contains("REJECTED")) {
            logger.info("Invalid input received: " + snippetEventsList.get(0).snippet().source());
            return output = ("Could not process input. Please verify the correctness of your statement.");
        }
        else {
            return snippetEventsList.get(0).value();
        }
    }
}
