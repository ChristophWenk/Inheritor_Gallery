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
        assertEquals("input.Person",instanceStatePM.getObjectPMs().get(0).getObjectTree().getFullClassName());

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
        assertEquals("input.Person",instanceStatePM.getObjectPMs().get(0).getObjectTree().getFullClassName());
    }


    @Test
    void testUpdateOverridenMethodsTree(){
        instanceStatePM.setJShellInput("Fahrzeug f = new Fahrzeug(\"velo\",20);");

        assertEquals("input.Item",instanceStatePM.getObjectPMs().get(0).getObjectTree().getSuperClass().getFullClassName());
        List<MethodPM> methods = instanceStatePM.getObjectPMs().get(0).getObjectTree().getSuperClass().getMethods();

        Optional<MethodPM> method = methods.stream()
                .filter(e -> e.getName().equals("print"))
                .findFirst();
        assertTrue(method.isPresent());
        assertEquals("input.Fahrzeug", method.get().getImplementedInClass());
    }

    @Test
    void testUpdateOverridenMethodsTree2(){
        instanceStatePM.setJShellInput("Auto a1 = new Auto(\"velo\",20,2,2);");
        //instanceStatePM.setJShellInput("Fahrzeug f1 = new Fahrzeug(\"velo\",20);");

        assertEquals("input.Item",instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getSuperClass().getFullClassName());

        List<MethodPM> methods = instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getSuperClass().getMethods();

        Optional<MethodPM> method = methods.stream()
                .filter(e -> e.getName().equals("print"))
                .findFirst();
        assertTrue(method.isPresent());
        assertEquals("input.Auto", method.get().getImplementedInClass());

        assertEquals(9,instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getMethods().size());

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
    void getObjectInterfaces(){
        //when
        instanceStatePM.setJShellInput("AntiqueBuyableFahrrad object = new AntiqueBuyableFahrrad(\"aa\",23,\"asd\",45);");

        //then
        assertEquals("AntiqueBuyableFahrrad",instanceStatePM.getObjectPMs().get(0).getObjectTree().getName());
        assertEquals("Fahrrad",instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getName());
        assertEquals("Fahrzeug",instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getSuperClass().getName());
        assertEquals(2,instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getImplementedInterfaces().size());
        assertEquals("Antique",instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getImplementedInterfaces().get(0).getName());
        assertEquals("Buyable",instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getImplementedInterfaces().get(1).getName());
        assertNull(instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getImplementedInterfaces().get(1).getSuperClass());
        assertEquals(1,instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getImplementedInterfaces().get(1).getMethods().size());
        assertEquals("getPrice",instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getImplementedInterfaces().get(1).getMethods().get(0).getName());


        assertEquals("input.AntiqueBuyableFahrrad",instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getImplementedInterfaces().get(0)
                .getMethods().get(0).getImplementedInClass());

    }


    @Test
    void getObjectPMPartsObjectTree() {
        //when
        //instanceStatePM.setJShellInput("Fahrzeug f1 = new Fahrzeug(\"tesla1\",11);");
        instanceStatePM.setJShellInput("Auto a1 = new Auto(\"tesla1\",1,2,3);");


        assertEquals("input.Auto", instanceStatePM.getObjectPMs().get(0).getObjectTree().getFullClassName());
        assertEquals("input.Fahrzeug", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getFullClassName());
        assertEquals("input.Item", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass()
                .getSuperClass().getFullClassName());
        assertEquals("ps", instanceStatePM.getObjectPMs().get(0).getObjectTree().getFields().get(0).getName());
        assertEquals("2", instanceStatePM.getObjectPMs().get(0).getObjectTree().getFields().get(0).getValue());

        assertEquals("speed", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getFields().get(0).getName());
        assertEquals("1.0", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getFields().get(0).getValue());

        assertEquals("weight", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getSuperClass().getFields().get(0).getName());
        assertEquals("0.0", instanceStatePM.getObjectPMs().get(0).getObjectTree()
                .getSuperClass().getSuperClass().getFields().get(0).getValue());

    }

}
