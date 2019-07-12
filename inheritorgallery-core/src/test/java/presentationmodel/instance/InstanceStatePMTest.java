package presentationmodel.instance;

import exceptions.InvalidCodeException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;
import service.jshell.ObjectDTO;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InstanceStatePMTest {
    private static InstanceStatePM instanceStatePM;
    private static UmlPM umlPM;
    private static JShellService jShellService = JShellService.getInstance();

    @BeforeAll
    public static void setUp() {
        umlPM = new UmlPM();
        instanceStatePM = new InstanceStatePM(umlPM);
    }

    @BeforeEach
    public void resetJShell() {
        jShellService.reset();
    }

    @Test
    void getObjectPMsTest(){
        //given

        //when
        instanceStatePM.setJShellInput("Person p1 = new Person();");

        //then
        assertEquals(1,instanceStatePM.getObjectPMs().size());
        assertEquals("input.Person",instanceStatePM.getObjectPMs().get(0).getObjectFullName());

        //when
        instanceStatePM.setJShellInput("Person p2 = new Person();");

        //then
        assertEquals(2,instanceStatePM.getObjectPMs().size());
    }

    @Test
    void getObjectPMsTestAddSameObject(){
        //given

        //when
        instanceStatePM.setJShellInput("Person p1 = new Person();");
        instanceStatePM.setJShellInput("Person p1 = new Person();");
        instanceStatePM.setJShellInput("Person p1 = new Person();");

        //then
        assertEquals(1,instanceStatePM.getObjectPMs().size());
        assertEquals("input.Person",instanceStatePM.getObjectPMs().get(0).getObjectFullName());
    }

    @Test
    void testSetObjectStructure(){
        //given

        //when
        instanceStatePM.setJShellInput("Person p1 = new Person();");
        instanceStatePM.setJShellInput("Fahrzeug f = new Fahrzeug(\"tesla\",20);");

        //then
        assertEquals(1,instanceStatePM.getObjectPMs().get(0).getObjectParts().size());
        assertEquals(2,instanceStatePM.getObjectPMs().get(1).getObjectParts().size());

        assertEquals("input.Item",instanceStatePM.getObjectPMs().get(1).getObjectParts().get(1).getFullClassName());
        assertEquals("input.Fahrzeug",instanceStatePM.getObjectPMs().get(1).getObjectParts().get(0).getFullClassName());
    }

    @Test
    void testSetFieldValues(){
        //given

        //when
        instanceStatePM.setJShellInput("Fahrzeug f = new Fahrzeug(\"tesla\",20);");

        //then
        assertEquals(2,instanceStatePM.getObjectPMs().get(0).getObjectParts().size());

        List<FieldPM> fields = instanceStatePM.getObjectPMs().get(0).getObjectParts().get(1).getFields();
        assertEquals("weight",fields.get(0).getName());
        assertEquals("0.0",fields.get(0).getValue());

    }

    @Test
    void testUpdateOverridenMethods(){
        instanceStatePM.setJShellInput("Fahrzeug f = new Fahrzeug(\"tesla\",20);");


        List<MethodPM> methods = instanceStatePM.getObjectPMs().get(0).getObjectParts().get(1).getMethods();
        Optional<MethodPM> method = methods.stream()
                .filter(e-> e.getDeclaredInClass().equals("input.Item") && e.getName().equals("print"))
                .findFirst();
        assertTrue(method.isPresent());
        assertEquals("input.Item", method.get().getDeclaredInClass());
        //assertEquals("input.Fahrzeug", method.get().getImplementedInClass());

    }

    @Test
    void testIsSameMethod(){
        instanceStatePM.setJShellInput("Fahrzeug f = new Fahrzeug(\"tesla\",20);");
        ObjectPM objectPM = instanceStatePM.getObjectPMs().get(0);

        List<MethodPM> methodsOfItem = instanceStatePM.getObjectPMs().get(0).getObjectParts().get(1).getMethods();
        Optional<MethodPM> method1 = methodsOfItem.stream()
                .filter(e->
                        e.getDeclaredInClass().equals("input.Item") &&
                                e.getName().equals("print")&&
                                e.getInputParameters().size() == 0)
                .findFirst();

        List<MethodPM> methodsOfFahrzeug = instanceStatePM.getObjectPMs().get(0).getObjectParts().get(0).getMethods();
        Optional<MethodPM> method2 = methodsOfFahrzeug.stream()
                .filter(e->
                            e.getDeclaredInClass().equals("input.Fahrzeug") &&
                            e.getName().equals("print")&&
                            e.getInputParameters().size() == 0)
                .findFirst();

        Optional<MethodPM> method3 = methodsOfFahrzeug.stream()
                .filter(e->
                        e.getDeclaredInClass().equals("input.Fahrzeug") &&
                                e.getName().equals("print")&&
                                e.getInputParameters().size() == 1)
                .findFirst();

        assertTrue(method1.isPresent());
        assertTrue(method2.isPresent());
        assertTrue(method3.isPresent());

        assertTrue(objectPM.isSameMethod(method1.get(),method2.get()));
        assertFalse(objectPM.isSameMethod(method1.get(),method3.get()));


    }

}
