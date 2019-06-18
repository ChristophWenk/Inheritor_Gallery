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
    public void testGetUmlClasses(){
        //given
        List<ClassDTO> classDTOs =  umlService.getClasses();
        //then
        assertNotNull(classDTOs);
        assertEquals(10, classDTOs.size());
        assertEquals("Person",classDTOs.get(9).getName());

    }
}