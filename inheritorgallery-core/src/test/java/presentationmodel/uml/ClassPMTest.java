package presentationmodel.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import presentationmodel.instance.InstanceStatePMTest;
import service.jshell.JShellService;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ClassPMTest {

    private static JShellService jShellService = JShellService.getInstance();
    private static UmlPM pm;

    @BeforeAll
    public static void setUp() {
        File file = new File(ClassPMTest.class.getClassLoader().getResource("testClasses.jar").getFile());
        jShellService.updateImports(file.toPath());
        pm = new UmlPM();
    }

    @Test
    void testClassToClassPM(){
        //given
        ClassPM classPMAntique = pm.getClassesObject().get(0);
        //then
        assertEquals("input.Antique",classPMAntique.getFullClassName());
        assertEquals("Antique",classPMAntique.getName());
        assertTrue(classPMAntique.isIsInterface());
        assertEquals(0,classPMAntique.getImplementedInterfacesAsString().size());
        assertNull(classPMAntique.getSuperClassName());
        assertNull(classPMAntique.getSuperClass());

        //given
        ClassPM classPMAntiqueBuyableFahrrad = pm.getClassesObject().get(1);
        //then
        assertEquals("AntiqueBuyableFahrrad",classPMAntiqueBuyableFahrrad.getName());
        assertFalse(classPMAntiqueBuyableFahrrad.isIsInterface());
        assertEquals(2,classPMAntiqueBuyableFahrrad.getImplementedInterfacesAsString().size());
        assertEquals("input.Antique",classPMAntiqueBuyableFahrrad.getImplementedInterfacesAsString().get(0));
        assertEquals("input.Buyable",classPMAntiqueBuyableFahrrad.getImplementedInterfacesAsString().get(1));
        assertEquals("input.Fahrrad",classPMAntiqueBuyableFahrrad.getSuperClassName());
        assertEquals("input.Fahrrad",classPMAntiqueBuyableFahrrad.getSuperClass().getFullClassName());

        //given
        ClassPM classPMAFahrrad = pm.getClassesObject().get(5);
        //then
        assertEquals("Fahrrad",classPMAFahrrad.getName());
        assertFalse(classPMAFahrrad.isIsInterface());
        assertEquals("input.Fahrzeug",classPMAFahrrad.getSuperClassName());
        assertEquals("input.Fahrzeug",classPMAFahrrad.getSuperClass().getFullClassName());
    }

    @Test
    void testClassToClassPMFields(){
        ClassPM classPMFahrzeug= pm.getClassesObject().get(6);

        assertEquals("private",classPMFahrzeug.getFields().get(0).getModifier());
        assertEquals("double",classPMFahrzeug.getFields().get(0).getType());
        assertEquals("speed",classPMFahrzeug.getFields().get(0).getName());

        assertEquals("package",classPMFahrzeug.getFields().get(1).getModifier());
        assertEquals("String",classPMFahrzeug.getFields().get(1).getType());
        assertEquals("name",classPMFahrzeug.getFields().get(1).getName());
    }

    @Test
    void testClassToClassDTOConstructors(){
        ClassPM classPMPerson= pm.getClassesObject().get(8);

        assertEquals("Person",classPMPerson.getConstructors().get(0).getName());

        assertEquals("public",classPMPerson.getConstructors().get(0).getModifier());
        assertEquals(0,classPMPerson.getConstructors().get(0).getInputParameters().size());

        assertEquals("package",classPMPerson.getConstructors().get(1).getModifier());
        assertEquals(1,classPMPerson.getConstructors().get(1).getInputParameters().size());

        assertEquals("private",classPMPerson.getConstructors().get(2).getModifier());
        assertEquals(2,classPMPerson.getConstructors().get(2).getInputParameters().size());

    }

    @Test
    void testClassToClassDTOMethods(){
        ClassPM classPMFahrzeug= pm.getClassesObject().get(6);

        assertEquals("input.Fahrzeug",classPMFahrzeug.getFullClassName());

        assertEquals(10,classPMFahrzeug.getMethods().size());
        assertEquals("public",classPMFahrzeug.getMethods().get(6).getModifier());
        assertEquals("void",classPMFahrzeug.getMethods().get(6).getReturnType());
        assertEquals("setDieselTax",classPMFahrzeug.getMethods().get(6).getName());
        assertEquals("double",classPMFahrzeug.getMethods().get(6).getInputParameters().get(0));

    }

}