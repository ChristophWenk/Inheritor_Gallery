package service.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.FileService;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UmlServiceTest {
    private static UmlService umlService;
    private static Path path;

    @BeforeAll
    public static void setUp() {
        umlService = new UmlService();
        FileService fileService = new FileService();
        path = fileService.getPath("/input");
    }

    @Test
    void testGetClassesForPath(){
        //given
        List<Class> classes = umlService.getClassesForPath(path);

        //then
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

    @Test
    void testGlassToClassDTO(){
        //given
        ClassDTO classDTO = umlService.getClassDTOs().get(0);

        //then
        assertEquals("Antique",classDTO.getName());
    }

    @Test
    public void testGetEdgeDTOs(){
        //given
        List<EdgeDTO> edgeDTOs =  umlService.getEdgeDTOs();
        //then
        assertNotNull(edgeDTOs);
        assertEquals(7, edgeDTOs.size());
        assertEquals("AntiqueBuyableFahrrad",edgeDTOs.get(0).getSource());
        assertEquals("Fahrrad",edgeDTOs.get(0).getTarget());
        assertEquals("extends",edgeDTOs.get(0).getType());


    }


}