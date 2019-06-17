package jshell.extension;

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import jdk.jshell.SourceCodeAnalysis;
import jshell.workingClasses.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class can execute jshell expressions in sequence
 * We can write java commands like shell script and execute it.
 * Just write commands in a way that we give in jshell and save it in a file and execute it.
 *
 * @author Hemamabara Vamsi, Kotari, extended by Wenk, Christoph & Muralt, Dimitri
 * @since 5/27/2017.
 */
public class JShellScriptExecutor {
    private JShell jshell;

    private static Logger logger = LoggerFactory.getLogger(JShellScriptExecutor.class);

    public JShellScriptExecutor() {
        jshell = JShell.create();
        jshell.addToClasspath("inheritorgallery-core/src/main/resources/jshell/classLibrary");
        jshell.eval("import jshell.workingClasses.*;");

        //showAllJShellMethods();
    }

    public void runExecutor(){
        String fileName = "inheritorgallery-core/src/main/resources/jshell/test_input.jsh";
        //evaluate(fileName);
        //executeSingleLineOfCode();
        jShellInteraction();
    }

    public void evaluate(String scriptFileName){
            // Handle snippet events. We can print value or take action if evaluation failed.
            jshell.onSnippetEvent(snippetEvent -> snippetEventHandler(snippetEvent));

        String scriptContent = null;
        try {
            scriptContent = new String(Files.readAllBytes(Paths.get(scriptFileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = scriptContent;
            while (true) {
                // Read source line by line till semicolon (;)
                SourceCodeAnalysis.CompletionInfo an = jshell.sourceCodeAnalysis().analyzeCompletion(s);
                if (!an.completeness().isComplete()) {
                    break;
                }
                // If there are any method declaration or class declaration in new lines, resolve it
                // otherwise execution errors will be thrown
                jshell.eval(trimNewlines(an.source()));
                // Exit if there are no more expressions to evaluate. EOF
                if (an.remaining().isEmpty()) {
                    break;
                }
                // If there is semicolon, execute next seq
                s = an.remaining();
            }
    }

    private void snippetEventHandler(SnippetEvent snippetEvent){
        String value = snippetEvent.value();
        if(!Objects.isNull(value) && value.trim().length() > 0) {
            // Prints output of code evaluation
            System.out.println(value);
        }

        // If there are any erros print and exit
        if(Snippet.Status.REJECTED.equals(snippetEvent.status())){
            System.out.println("Evaluation failed : "+snippetEvent.snippet().toString()+"\nIgnoring execution of above script");
        }
    }

    private String trimNewlines(String s) {
        int b = 0;
        while (b < s.length() && s.charAt(b) == '\n') {
            ++b;
        }
        int e = s.length() -1;
        while (e >= 0 && s.charAt(e) == '\n') {
            --e;
        }
        return s.substring(b, e + 1);
    }

    public void executeSingleLineOfCode() {
        String input = "System.out.println(\"Very simple test\")";
        jshell.eval(input);
    }

    public void jShellInteraction() {
        String input;

        // Infinite JShell evaluation loop
        logger.info("Beginning evaluation loop");
        Scanner scanner = new Scanner(System.in);
        while((input = scanner.nextLine()) != null) {
            // Try the following input:
            // System.out.println("Test");
            if (input.contains("//") || input.contains("/*")) {
                logger.info("Comment received. Asking user for new input.");
                System.out.println("Comments are not allowed as input. Remove comments and try again.");
                continue;
            }

            List<SnippetEvent> snippetEventsList = jshell.eval(input);
            if (snippetEventsList.get(0).status().name().contains("REJECTED")) {
                logger.info("Invalid input received: " + snippetEventsList.get(0).snippet().source());
                System.out.println("Could not process input. Please verify the correctness of your statement.");
            }
            else {
            }
        }
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



    private void showAllJShellMethods() {
        List<SnippetEvent> snippetEventList =
                jshell.eval("Person p = new Person();");

        jshell.eval("System.out.println(p.getFirstName());");

        // How to retrieve all kinds of information about the snippet
        SnippetEvent event = snippetEventList.get(0);
        Snippet snippet = event.snippet();
        Snippet.Kind kind = snippet.kind();
        String source = snippet.source();

        System.out.println("Size of EventList: " + snippetEventList.size());
        System.out.println("Value of Event: " + snippetEventList.get(0).value());
        System.out.println("Snippet content: " + snippet); 
        System.out.println("Snippet type: " + kind); // Variable / Import / Method
        System.out.println("Snippet source code: " + source);

        Person t = new Person();
        System.out.println(t.getFirstName());
    }

}
