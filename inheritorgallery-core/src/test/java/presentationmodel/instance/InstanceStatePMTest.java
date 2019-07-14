package presentationmodel.instance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InstanceStatePMTest {
    private static InstanceStatePM instanceStatePM;
    private static UmlPM umlPM;
    private static JShellService jShellService = JShellService.getInstance();
    private static Logger logger = LoggerFactory.getLogger(InstanceStatePMTest.class);

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
        assertEquals("input.Person",instanceStatePM.getObjectPMs().get(0).getObjectRootClass().getFullClassName());

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
        assertEquals("input.Person",instanceStatePM.getObjectPMs().get(0).getObjectRootClass().getFullClassName());
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
        instanceStatePM.setJShellInput("Fahrzeug f1 = new Fahrzeug(\"tesla1\",10);");
        instanceStatePM.setJShellInput("f1.setWeight(1);");
        instanceStatePM.setJShellInput("Auto a1 = new Auto(\"tesla2\",20,4,5);");
        instanceStatePM.setJShellInput("a1.setWeight(2);");

        //then
        assertEquals(2,instanceStatePM.getObjectPMs().get(0).getObjectParts().size());

        List<FieldPM> fields = instanceStatePM.getObjectPMs().get(0).getObjectParts().get(1).getFields();
        assertEquals("weight",fields.get(0).getName());
        assertEquals("1.0",fields.get(0).getValue());

        List<FieldPM> fields2 = instanceStatePM.getObjectPMs().get(1).getObjectParts().get(2).getFields();
        assertEquals("weight",fields2.get(0).getName());
        //assertEquals("2.0",fields2.get(0).getValue());
    }

    @Test
    void testUpdateOverridenMethods(){
        instanceStatePM.setJShellInput("Fahrzeug f = new Fahrzeug(\"velo\",20);");


        List<MethodPM> methods = instanceStatePM.getObjectPMs().get(0).getObjectParts().get(1).getMethods();
        Optional<MethodPM> method = methods.stream()
                .filter(e -> e.getName().equals("print"))
                .findFirst();
        assertTrue(method.isPresent());
        assertEquals("input.Fahrzeug", method.get().getImplementedInClass());
    }

    @Test
    void testGetReferencesForObject(){
        //given
        instanceStatePM.setJShellInput("Fahrzeug f1 = new Fahrzeug(\"velo\",20);");
        instanceStatePM.setJShellInput("Fahrzeug f2 = new Fahrzeug(\"velo\",20);");
        instanceStatePM.setJShellInput("Fahrzeug f3 = f1;");

        //when
        ReferencePM reference1 = instanceStatePM.getObjectPMs().get(0).getReferences().get(0);
        //then
        assertEquals("f1", reference1.getReferenceName());
        assertEquals("Fahrzeug", reference1.getReferenceType());

        //when
        ReferencePM reference2 = instanceStatePM.getObjectPMs().get(1).getReferences().get(0);
        //then
        assertEquals("f2", reference2.getReferenceName());
        assertEquals("Fahrzeug", reference2.getReferenceType());

        //when
        ReferencePM reference3 = instanceStatePM.getObjectPMs().get(0).getReferences().get(1);
        //then
        assertEquals("f3", reference3.getReferenceName());
        assertEquals("Fahrzeug", reference3.getReferenceType());
    }

    @Test
    void getObjectPMParts(){
        //when
        instanceStatePM.setJShellInput("Fahrzeug object = new Fahrzeug(\"tesla\",23);");

        //then
        assertEquals(2,instanceStatePM.getObjectPMs().get(0).getObjectParts().size());
        assertEquals(1,instanceStatePM.getObjectPMs().get(0).getObjectWidth());

        //when
        instanceStatePM.setJShellInput("AntiqueBuyableFahrrad object = new AntiqueBuyableFahrrad(\"aa\",23,\"asd\",45);");

        //then
        assertEquals(6,instanceStatePM.getObjectPMs().get(0).getObjectParts().size());
        assertEquals(3,instanceStatePM.getObjectPMs().get(0).getObjectWidth());
    }

    @Test
    void getObjectPMParts2() {
        //when
        instanceStatePM.setJShellInput("Fahrzeug f1 = new Fahrzeug(\"tesla1\",11);");
        instanceStatePM.setJShellInput("Fahrzeug f2 = new Fahrzeug(\"tesla2\",22);");
        instanceStatePM.setJShellInput("f2.setWeight(2);");

        //then
        assertEquals(2, instanceStatePM.getObjectPMs().get(0).getObjectParts().size());
        assertEquals(1, instanceStatePM.getObjectPMs().get(0).getObjectWidth());


        assertEquals("input.Fahrzeug", instanceStatePM.getObjectPMs().get(0).getObjectRootClass().getFullClassName());
        assertEquals("input.Item", instanceStatePM.getObjectPMs().get(0).getObjectRootClass()
                .getSuperClass().getFullClassName());
//        assertEquals("2.0", instanceStatePM.getObjectPMs().get(0).getObjectRootClass()
//                .getSuperClass().getFields().get(0).getName());

    }
    @Test
    void getObjectPMParts3() {
        //when
        //instanceStatePM.setJShellInput("Fahrzeug f1 = new Fahrzeug(\"tesla1\",11);");
        instanceStatePM.setJShellInput("Auto a1 = new Auto(\"tesla1\",1,2,3);");

        //then
        assertEquals(3, instanceStatePM.getObjectPMs().get(0).getObjectParts().size());
        assertEquals(1, instanceStatePM.getObjectPMs().get(0).getObjectWidth());


        assertEquals("input.Auto", instanceStatePM.getObjectPMs().get(0).getObjectTree().getFullClassName());
        assertEquals("input.Fahrzeug", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getFullClassName());
        assertEquals("input.Item", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass()
                .getSuperClass().getFullClassName());
//        assertEquals("2.0", instanceStatePM.getObjectPMs().get(0).getObjectRootClass()
//                .getSuperClass().getFields().get(0).getName());

    }

}
