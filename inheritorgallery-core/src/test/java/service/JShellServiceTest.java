package service;

import exceptions.InvalidCodeException;
import jdk.jshell.Snippet;
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
    void testEvaluateCode() {
        String input = "Item i = new Fahrzeug(\"tesla\", 20);";

        Snippet snippet = null;
        try {
            snippet = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        assertNotNull(snippet);
    }

    @Test
    void testGetRefName() {
        String input = "Item i = new Fahrzeug(\"tesla\", 20);";
        Snippet snippet = null;
        try {
            snippet = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        assertEquals("i",jShellService.getRefName(snippet));
    }

    @Test
    void testGetRefType() {
        //given
        String input = "Item i1 = new Fahrzeug(\"tesla\", 20);";
        Snippet snippet = null;
        try {
            snippet = jShellService.evaluateCode(input);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        //then
        assertEquals("Item",jShellService.getRefType(snippet));
    }

    @Test
    void testGetRefTypeSecondReference() {
        //given
        String input1 = "Fahrzeug i1 = new Fahrzeug(\"tesla\", 20);";
        String input2 = "Item i2 = i1;";
        Snippet snippet1 = null;
        Snippet snippet2 = null;
        try {
            snippet1 = jShellService.evaluateCode(input1);
            snippet2 = jShellService.evaluateCode(input2);
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }
        //then
        assertEquals("Fahrzeug",jShellService.getRefType(snippet1));
        assertEquals("Item",jShellService.getRefType(snippet2));
    }

    @Test
    void testGetClass() {
        //given
//        String input =
//                "Item i1 = new Fahrzeug(\"tesla\", 20);"+
//                        "import java.lang.reflect.Method;" +
//                        "import java.lang.reflect.Field;";
//
//        try {
//            jShellService.evaluateCode(input);
//        } catch (InvalidCodeException e) {
//            e.printStackTrace();
//        }

        //then
        assertEquals("Fahrzeug",jShellService.getClassForReference("i1"));
    }

    @Test
    void testGetMethods(){
        String input =
                "Person p = new Person();"+
                "import java.lang.reflect.Method;" +
                "import java.lang.reflect.Field;";

        jShellService.processInput(input);

        //order of methods retured is not always the same
        //input = "p.getClass().getMethods()[0].getName();";
        //assertEquals("\"toString\"", jShellService.processInput(input));

//        input = "Class cls = null;" +
//                "try {Class cls = Class.forName(\"input.Person\");" +
//                "        } catch (ClassNotFoundException e) { e.printStackTrace();}";
//
//        assertEquals("null", jShellService.processInput(input));
//
//        assertEquals("sayGreeting", jShellService.getInstances());

    }

    @Test
    void testGetInstancesLocal(){
        jShellService.testGetInstancesLocal();


    }

}