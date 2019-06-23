package presentationmodel.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.UmlService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UmlPMTest {
    private static UmlService umlService;
    private static UmlPM pm;

    @BeforeAll
    public static void setUp() {

        umlService = new UmlService();
        pm = new UmlPM(umlService);
    }

    @Test
    public void getClasses(){
        //given
        //UmlPM umlPM = umlService.createUmlPM();

        //then
        assertEquals(10, pm.getClasses().size());

        ClassPM classPM = pm.getClasses().get(9);
        assertEquals("Person", classPM.getName());
        assertEquals(2, classPM.getFields().size());
        assertEquals(2, classPM.getConstructors().size());
        assertEquals(6, classPM.getMethods().size());
    }

    @Test
    public void testGetAllEdges(){
        //given

        //then

        assertEquals(7, pm.getEdges().size());
    }

    @Test
    public void testGetClassByName(){
        //given

        //then
        assertEquals(pm.getClasses().get(9), pm.getClassByName("Person"));

    }
    @Test
    public void testSetClassInheritanceLevel(){
        //given

        //then
        assertEquals(3,pm.getInheritanceDeepness());

        assertEquals(0, pm.getInheritanceLevel().get(pm.getClassByName("Item")));
        assertEquals(1, pm.getInheritanceLevel().get(pm.getClassByName("Fahrzeug")));
        assertEquals(2, pm.getInheritanceLevel().get(pm.getClassByName("Fahrrad")));
        assertEquals(3, pm.getInheritanceLevel().get(pm.getClassByName("Cabriolet")));
        assertEquals(3, pm.getInheritanceLevel().get(pm.getClassByName("Person")));


    }
}