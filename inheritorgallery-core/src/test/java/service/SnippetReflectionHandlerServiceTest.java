package service;

import exceptions.InvalidCodeException;
import jdk.jshell.SnippetEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SnippetReflectionHandlerServiceTest {

    private static SnippetReflectionHandlerService handlerService;
    private static JShellService jShellService = JShellService.getInstance();

    @BeforeAll
    static void setup() {
        handlerService = new SnippetReflectionHandlerService();
        jShellService = JShellService.getInstance();
    }

    @Test
    void testGetClassName() {
        String input = "Auto auto = new Auto();";
        List<SnippetEvent> snippetEventsList = null;
        try {
            snippetEventsList = jShellService.getSnippetEventsList(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        assertEquals("Auto",handlerService.getClassName(snippetEventsList.get(0)));
    }

    @Test
    void testGetMethods() {
        //String input = "Auto auto = new Auto();";
        String input = "Person p = new Person();";
        //List<SnippetEvent> snippetEventsList = jShellService.getSnippetEventsList(input);

        String s = jShellService.processInput(input);

        //assertEquals("Auto",handlerService.getClassMethods(snippetEventsList.get(0)));
    }

}