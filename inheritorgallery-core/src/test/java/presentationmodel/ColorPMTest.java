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
        assertEquals("#EF5350",colorPM.getColor("input.Fahrzeug"));
        assertEquals("#EF5350",colorPM.getColor("input.Fahrzeug"));

    }

    @Test
    void testGetNextColor() {
        assertEquals("#EF5350",colorPM.getNextColor());
    }

    @Test
    void testMapColor() {
        colorPM.mapColor("input.Fahrzeug");
        assertEquals("#EF5350",colorPM.getObjectColorMap().get("input.Fahrzeug"));
    }

    @Test
    void testGetObjectColorMap() {
        colorPM.mapColor("input.Fahrzeug");
        assertEquals("#EF5350",colorPM.getObjectColorMap().get("input.Fahrzeug"));
        colorPM.mapColor("input.Fahrzeug");
        assertEquals(1,colorPM.getObjectColorMap().size());
        colorPM.mapColor("input.Auto");
        assertEquals(2,colorPM.getObjectColorMap().size());
    }

    @Test
    void testGetColorList() {
        assertEquals("#66BB6A",colorPM.getColorList().get(9));
    }
}