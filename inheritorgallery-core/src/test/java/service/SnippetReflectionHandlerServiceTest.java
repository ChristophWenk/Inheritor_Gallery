package service;

import org.junit.jupiter.api.BeforeAll;

class SnippetReflectionHandlerServiceTest {

    private static SnippetReflectionHandlerService handlerService;
    private static JShellService jShellService = JShellService.getInstance();

    @BeforeAll
    static void setup() {
        handlerService = new SnippetReflectionHandlerService();
        jShellService = JShellService.getInstance();
    }
/*
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
        String input = "Auto auto = new Auto();";
        List<SnippetEvent> snippetEventsList = null;
        try {
            snippetEventsList = jShellService.getSnippetEventsList(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        assertEquals("Auto",handlerService.getClassMethods(snippetEventsList.get(0)));
    }
*/
}