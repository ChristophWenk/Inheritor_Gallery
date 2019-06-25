package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JShellServiceTest {

    private static JShellService jShellService = JShellService.getInstance();

    @Test
    void evaluateCode() {

    }

    @Test
    void testProcessInput() {
        String input = "Person p = new Person();";
        assertEquals("Chris Wenk",jShellService.processInput(input));

        input = "Fahrzeug f = new Fahrzeug(\"tesla\",20);";
        assertEquals("Fahrzeug tesla fährt 20.0",jShellService.processInput(input));

    }

}