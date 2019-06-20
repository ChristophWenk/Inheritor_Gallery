package service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UmlServiceTest {
    private static UmlService umlService;

    @BeforeAll
    public static void setUp() {
        umlService = new UmlService();
    }

    @Test
    public void testGetClassDTOs(){
        //given
        List<ClassDTO> classDTOs =  umlService.getClassDTOs();
        //then
        assertNotNull(classDTOs);
        assertEquals(10, classDTOs.size());
        assertEquals("Person",classDTOs.get(9).getName());

    }

    @Test
    public void testGetEdgeDTOs(){
        //given
        List<EdgeDTO> edgeDTOs =  umlService.getEdgeDTOs();
        //then
        assertNotNull(edgeDTOs);
        assertEquals(7, edgeDTOs.size());
        assertEquals("Fahrzeug",edgeDTOs.get(edgeDTOs.size()-1).getSource());
        assertEquals("Item",edgeDTOs.get(edgeDTOs.size()-1).getTarget());
        assertEquals("extends",edgeDTOs.get(edgeDTOs.size()-1).getType());


    }
}