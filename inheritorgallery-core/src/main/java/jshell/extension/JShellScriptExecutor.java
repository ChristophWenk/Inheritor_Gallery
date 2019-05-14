package jshell.extension;

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import jdk.jshell.SourceCodeAnalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    JShell jshell;

    public JShellScriptExecutor() {
        jshell = JShell.create();
    }

    public void runExecutor(){
        String fileName = "inheritorgallery-core/src/main/resources/jshell/test_input.jsh";
        evaluate(fileName);
        executeSingleLineOfCode();
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

    public void snippetEventHandler(SnippetEvent snippetEvent){
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

    private void executeSingleLineOfCode() {
        String input;

        input = "System.out.println(\"Very simple test\")";
        JShell jshell = JShell.create();
        jshell.eval(input);
    }

    private void jShellInteraction() {
        String input;
        String fileName = "inheritorgallery-core/src/main/resources/jshell/classImport.jsh";
        evaluate(fileName);

        Scanner scanner = new Scanner(System.in);
        while((input = scanner.nextLine()) != null) {
            // Try the following input:
            // System.out.println("Perfect Test of this method");
            jshell.eval(input);
        }
    }

}
