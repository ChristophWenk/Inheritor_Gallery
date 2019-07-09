package service;

import org.junit.jupiter.api.BeforeAll;
import service.jshell.JShellService;
import service.jshell.SnippetReflectionHandlerService;

class SnippetReflectionHandlerServiceTest {

    private static SnippetReflectionHandlerService handlerService;
    private static JShellService jShellService = JShellService.getInstance();

    @BeforeAll
    static void setup() {
        handlerService = new SnippetReflectionHandlerService();
        jShellService = JShellService.getInstance();
    }
}