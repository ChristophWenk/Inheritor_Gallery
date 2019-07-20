package presentationmodel.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.jshell.JShellService;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UmlPMTest {
    private static JShellService jShellService = JShellService.getInstance();
    private static UmlPM pm;

    @BeforeAll
    public static void setUp() {
        File file = new File("src/test/resources/testClasses.jar");
        String jarStringPath = file.toURI().toString();
        jShellService.updateImports(jarStringPath.replace("%20"," "));
        pm = new UmlPM();
    }

    @Test
    public void getClasses(){
        //given
        //UmlPM umlPM = umlService.createUmlPM();

        //then
        assertEquals(9, pm.getClassesObject().size());

        ClassPM classPM = pm.getClassesObject().get(8);
        assertEquals("Person", classPM.getName());
        assertEquals(2, classPM.getFields().size());
        assertEquals(3, classPM.getConstructors().size());
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
        assertEquals(pm.getClassesObject().get(8), pm.getClassByName("Person"));

    }
    @Test
    public void testSetClassInheritanceLevel(){

        assertEquals(0, pm.getClassByName("Item").getInheritanceLevel());
        assertEquals(1, pm.getClassByName("Fahrzeug").getInheritanceLevel());
        assertEquals(2, pm.getClassByName("Fahrrad").getInheritanceLevel());
        assertEquals(3, pm.getClassByName("Cabriolet").getInheritanceLevel());
        assertEquals(3, pm.getClassByName("Person").getInheritanceLevel());

    }

    @Test
    public void testGetInterfaces(){

        assertEquals("Antique", pm.getClassesObject().get(0).getName());
        assertEquals("AntiqueBuyableFahrrad", pm.getClassesObject().get(1).getName());
        assertEquals("Auto", pm.getClassesObject().get(2).getName());

        assertEquals(0, pm.getClassesObject().get(2).getImplementedInterfacesAsString().size());
        assertEquals(0, pm.getClassesObject().get(2).getImplementedInterfaces().size());

        assertEquals(2, pm.getClassesObject().get(1).getImplementedInterfacesAsString().size());
        assertEquals(2, pm.getClassesObject().get(1).getImplementedInterfaces().size());

    }

}