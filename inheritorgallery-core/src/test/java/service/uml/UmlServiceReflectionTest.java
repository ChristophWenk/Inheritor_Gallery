package service.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.FileService;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UmlServiceReflectionTest {
    private static UmlServiceReflection umlServiceReflection;

    @BeforeAll
    public static void setUp() {
        umlServiceReflection = new UmlServiceReflection();
    }

    @Test
    void testGetClassesForPath(){
        FileService fileService = new FileService();
        Path path = fileService.getPath("/input");

        List<Class> classes = umlServiceReflection.getClassesForPath(path);

        assertEquals(9,classes.size());
        assertEquals("input.Antique",classes.get(0).getCanonicalName());
        assertEquals("input.AntiqueBuyableFahrrad",classes.get(1).getCanonicalName());
        assertEquals("input.Auto",classes.get(2).getCanonicalName());
        assertEquals("input.Buyable",classes.get(3).getCanonicalName());
        assertEquals("input.Cabriolet",classes.get(4).getCanonicalName());
        assertEquals("input.Fahrrad",classes.get(5).getCanonicalName());
        assertEquals("input.Fahrzeug",classes.get(6).getCanonicalName());
        assertEquals("input.Item",classes.get(7).getCanonicalName());
        assertEquals("input.Person",classes.get(8).getCanonicalName());


    }


}