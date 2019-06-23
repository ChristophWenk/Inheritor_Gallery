import exceptions.InvalidCodeException;
import jdk.jshell.SnippetEvent;
import service.JShellService;
import service.SnippetReflectionHandlerService;

import java.util.List;

public class Test {

    private static JShellService jShellService = JShellService.getInstance();
    private static SnippetReflectionHandlerService handlerService = new SnippetReflectionHandlerService();

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        String input = "Fahrzeug auto = new Auto(\"Model S\", 250.7, 300, 3);";
        //String input = "Person p = new Person();";
        List<SnippetEvent> snippetEventsList = null;
        try {
            snippetEventsList = jShellService.getSnippetEventsList(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        handlerService.getMethods(snippetEventsList.get(0));
    }

    public static void simpleTest() {
        String input = "Person p = new Person();";
        String s = jShellService.processInput(input);
        System.out.println(s);
    }
}
