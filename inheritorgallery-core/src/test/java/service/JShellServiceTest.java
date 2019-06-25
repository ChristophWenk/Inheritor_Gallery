package service;

import org.junit.jupiter.api.Test;
import service.jshell.JShellService;

import static org.junit.jupiter.api.Assertions.*;

class JShellServiceTest {

    private static JShellService jShellService = JShellService.getInstance();

    @Test
    void testProcessInput() {
        String input = "Person p = new Person();";
        assertEquals("Chris Wenk",jShellService.processInput(input));

        input = "Fahrzeug f = new Fahrzeug(\"tesla\",20);";
        assertEquals("Fahrzeug tesla fährt 20.0",jShellService.processInput(input));

        input = "wrong input";
        assertEquals("Could not process input. Please verify the correctness of your statement.",jShellService.processInput(input));

        input = "//";
        assertEquals("Comments are not allowed as input. Remove comments and try again.",jShellService.processInput(input));
    }

    @Test
    void testGetInstances(){
        String input =
                "Person p = new Person();"+
                "import java.lang.reflect.Method;";
        jShellService.processInput(input);

        input = "p.getClass().getMethods()[0].getName();";
        assertEquals("\"toString\"", jShellService.processInput(input));

    }

}