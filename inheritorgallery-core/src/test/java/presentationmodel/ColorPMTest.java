package presentationmodel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorPMTest {

    private ColorPM colorPM = new ColorPM();

    @BeforeAll
    public static void setUp() {
    }

    @Test
    void testGetColor() {
        assertEquals("#8EBA43",colorPM.getColor("input.Fahrzeug"));
        assertEquals("#8EBA43",colorPM.getColor("input.Fahrzeug"));

    }

    @Test
    void testGetNextColor() {
        assertEquals("#8EBA43",colorPM.getNextColor());
    }

    @Test
    void testMapColor() {
        colorPM.mapColor("input.Fahrzeug");
        assertEquals("#8EBA43",colorPM.getObjectColorMap().get("input.Fahrzeug"));
    }

    @Test
    void testGetObjectColorMap() {
        colorPM.mapColor("input.Fahrzeug");
        assertEquals("#8EBA43",colorPM.getObjectColorMap().get("input.Fahrzeug"));
        colorPM.mapColor("input.Fahrzeug");
        assertEquals(1,colorPM.getObjectColorMap().size());
        colorPM.mapColor("input.Auto");
        assertEquals(2,colorPM.getObjectColorMap().size());
    }

    @Test
    void testGetColorList() {
        assertEquals("#5C6BC0",colorPM.getColorList().get(9));
    }
}