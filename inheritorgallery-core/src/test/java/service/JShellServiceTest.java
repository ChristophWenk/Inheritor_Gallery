package service;

import exceptions.InvalidCodeException;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;
import org.junit.jupiter.api.Test;
import service.jshell.JShellService;

import static org.junit.jupiter.api.Assertions.*;

class JShellServiceTest {

    private static JShellService jShellService = JShellService.getInstance();

    @Test
    void testEvaluateCode() {
        String input = "Item i = new Fahrzeug(\"tesla\", 20);";

        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode(input);
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
    void testGetInstanceDTO() {
        //String input1 = "Fahrzeug f = new Fahrzeug(\"tesla\", 20);";

        String input =  "int i = 3;";

        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        //VarSnippet varSnippet = (VarSnippet) snippetEvent.snippet();
        snippetEvent.snippet().subKind();


        assertEquals("VAR", snippetEvent.snippet().subKind().kind().toString());
        assertEquals("VAR_DECLARATION_WITH_INITIALIZER_SUBKIND", snippetEvent.snippet().subKind().toString());
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
        String input =
                "Item i1 = new Fahrzeug(\"tesla\", 20);";
        try {
            jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        //then
        assertEquals("Fahrzeug",jShellService.getClassForReference("i1"));
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