package presentationmodel.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.UmlService;

import static org.junit.jupiter.api.Assertions.*;

class UmlPMTest {
    private static UmlService umlService;

    @BeforeAll
    public static void setUp() {
        umlService = new UmlService();
    }

    @Test
    public void testGetUmlClasses(){
        //given
        UmlPM umlPM = umlService.createUmlPM();
        //then
        assertEquals("Person", umlPM.getClasses().get(0).getName());
    }
}