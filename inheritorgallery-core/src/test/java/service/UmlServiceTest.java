package service;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.UmlPM;

import static org.junit.jupiter.api.Assertions.*;

class UmlServiceTest {
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

        assertNotNull(umlPM);
        assertEquals("Person", umlPM.getClasses().get(0).getName());

    }
}