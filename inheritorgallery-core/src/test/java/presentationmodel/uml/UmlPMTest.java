package presentationmodel.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.UmlService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testGetEdges(){
        //given
        List<EdgePM> edgePM = pm.getEdges();

        //then
        assertEquals(7, edgePM.size());
        assertEquals("Fahrzeug",edgePM.get(edgePM.size()-1).getSource());
        assertEquals("Item",edgePM.get(edgePM.size()-1).getTarget());
        assertEquals("extends",edgePM.get(edgePM.size()-1).getType());

    }
}