package presentationmodel.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassPMTest {

    private static UmlPM pm;

    @BeforeAll
    public static void setUp() {
        pm = new UmlPM();
    }

    @Test
    void testGlassToClassDTO(){
        //given
        ClassPM classPMAntique = pm.getClasses().get(0);
        //then
        assertEquals("input.Antique",classPMAntique.getFullClassName());
        assertEquals("Antique",classPMAntique.getName());
        assertTrue(classPMAntique.isIsInterface());
        assertEquals(0,classPMAntique.getImplementedInterfaces().size());
        assertNull(classPMAntique.getSuperClassName());

        //given
        ClassPM classPMAntiqueBuyableFahrrad = pm.getClasses().get(1);
        //then
        assertEquals("AntiqueBuyableFahrrad",classPMAntiqueBuyableFahrrad.getName());
        assertFalse(classPMAntiqueBuyableFahrrad.isIsInterface());
        assertEquals(2,classPMAntiqueBuyableFahrrad.getImplementedInterfaces().size());
        assertEquals("input.Antique",classPMAntiqueBuyableFahrrad.getImplementedInterfaces().get(0));
        assertEquals("input.Buyable",classPMAntiqueBuyableFahrrad.getImplementedInterfaces().get(1));
        assertEquals("input.Fahrrad",classPMAntiqueBuyableFahrrad.getSuperClassName());

        //given
        ClassPM classPMAFahrrad = pm.getClasses().get(5);
        //then
        assertEquals("Fahrrad",classPMAFahrrad.getName());
        assertFalse(classPMAFahrrad.isIsInterface());
        assertEquals("input.Fahrzeug",classPMAFahrrad.getSuperClassName());
    }

    @Test
    void testClassToClassDTOFields(){
        ClassPM classPMFahrzeug= pm.getClasses().get(6);

        assertEquals("private",classPMFahrzeug.getFields().get(0).getModifier());
        assertEquals("double",classPMFahrzeug.getFields().get(0).getType());
        assertEquals("speed",classPMFahrzeug.getFields().get(0).getName());

        assertEquals("package",classPMFahrzeug.getFields().get(1).getModifier());
        assertEquals("String",classPMFahrzeug.getFields().get(1).getType());
        assertEquals("name",classPMFahrzeug.getFields().get(1).getName());
    }

}