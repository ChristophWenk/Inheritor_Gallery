package service;

import exceptions.InvalidCodeException;
import jdk.jshell.SnippetEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.jshell.JShellService;

import static org.junit.jupiter.api.Assertions.*;

class JShellServiceTest {

    private static JShellService jShellService = JShellService.getInstance();

    @BeforeEach
    public void resetJShell() {
        jShellService.reset();
    }

    @Test
    void testEvaluateCode() {
        String input1 = "Item i = new Fahrzeug(\"tesla\", 20);";
        String input2    =    "i.getWeight();";


        SnippetEvent snippetEvent = null;
        try {
            jShellService.evaluateCode(input1);
            snippetEvent = jShellService.evaluateCode(input2);

        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        assertNotNull(snippetEvent);
    }

    @Test
    void testCleanseInput() {
        String input = "//";

        assertThrows(InvalidCodeException.class, () -> {
            jShellService.evaluateCode(input);
        });
    }

    @Test
    void testGetOutputAsString() {
        String input1 = "Fahrzeug f = new Fahrzeug(\"tesla\", 20);";
        String input2  = "f.getName();";

        SnippetEvent snippetEvent = null;
        try {
            jShellService.evaluateCode(input1);
            snippetEvent = jShellService.evaluateCode(input2);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        assertEquals("\"tesla\"", jShellService.getOutputAsString(snippetEvent));
    }

    @Test
    void testGetObjectDTO() {
        String input1 = "Fahrzeug f = new Fahrzeug(\"tesla\", 20);";
        String input2 = "Fahrzeug f2 = new Fahrzeug(\"tesla2\", 20);";
        String input3 = "Item a;";
        String input4 = "a = f;";
        String input5 = "Item i = new Fahrzeug(\"teslaToBeOverridden\", 20);";
        String input6 = "int i = 3;";

        try {
            jShellService.evaluateCode(input1);
            jShellService.evaluateCode(input2);
            jShellService.evaluateCode(input3);
            jShellService.evaluateCode(input4);
            jShellService.evaluateCode(input5);
            jShellService.evaluateCode(input6);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        assertEquals(2,jShellService.getObjectDTOs().size());
    }

    @Test
    void testGetRefName() {
        String input = "Item i = new Fahrzeug(\"tesla\", 20);";
        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        assertEquals("i",jShellService.getRefName(snippetEvent));
    }

    @Test
    void testGetRefType() {
        //given
        String input = "Item i1 = new Fahrzeug(\"tesla\", 20);";
        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        //then
        assertEquals("Item",jShellService.getRefType(snippetEvent));
    }

    @Test
    void testGetRefTypeSecondReference() {
        //given
        String input1 = "Fahrzeug i1 = new Fahrzeug(\"tesla\", 20);";
        String input2 = "Item i2 = i1;";
        SnippetEvent snippetEvent1 = null;
        SnippetEvent snippetEvent2 = null;
        try {
            snippetEvent1 = jShellService.evaluateCode(input1);
            snippetEvent2 = jShellService.evaluateCode(input2);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        //then
        assertEquals("Fahrzeug",jShellService.getRefType(snippetEvent1));
        assertEquals("Item",jShellService.getRefType(snippetEvent2));
    }

    @Test
    void testGetClassForReference() {
        //given
        String input1 = "Item i1 = new Fahrzeug(\"tesla\", 20);";
        String input2 = "Fahrzeug f = new Fahrzeug(\"f2\", 20);";
        String input3 = "Item a;";
        String input4 = "a = f;";
        try {
            jShellService.evaluateCode(input1);
            jShellService.evaluateCode(input2);
            jShellService.evaluateCode(input3);
            jShellService.evaluateCode(input4);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        //then
        assertEquals("Fahrzeug",jShellService.getClassForReference("i1"));
        assertEquals("Fahrzeug",jShellService.getClassForReference("f"));
        assertEquals("Fahrzeug",jShellService.getClassForReference("a"));
    }

    @Test
    void testGetHashcodeForReference() {
        //given
        String input = "Item i1 = new Fahrzeug(\"tesla\", 20);";

        try {
            jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        //then
        assertNotNull(jShellService.getHashcodeForReference("i1"));
    }

    @Test
    void testGetPackageForReference() {
        //given
        String input =
                "Item i1 = new Fahrzeug(\"tesla\", 20);";
        try {
            jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        //then
        assertEquals("input",jShellService.getPackageForReference("i1"));
    }

    @Test
    void testGetInstancesLocal(){
        jShellService.testGetInstancesLocal();


    }

}