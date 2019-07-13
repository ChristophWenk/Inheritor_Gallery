package service.uml;

import exceptions.InvalidCodeException;
import jdk.jshell.SnippetEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileService;
import service.jshell.JShellService;
import service.jshell.dto.ClassDTO;
import jshellExtensions.JShellReflection;

import java.io.*;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JShellReflectionTest {
    private static JShellReflection jshellReflection;
    private static JShellService jShellService = JShellService.getInstance();
    private static Path path;

    @BeforeEach
    public void resetJShell() {
        jShellService.reset();
    }

    @BeforeAll
    public static void setUp() {
        jshellReflection = new JShellReflection();
        FileService fileService = new FileService();
        path = fileService.getPath("/input");
    }

    @Test
    void testGetClassesForPath(){
        //given
        List<Class> classes = jshellReflection.getClassesForPath(path);

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
        ClassDTO classDTOAntique = jshellReflection.getClassDTOs().get(0);
        //then
        assertEquals("input.Antique",classDTOAntique.getFullClassName());
        assertEquals("Antique",classDTOAntique.getSimpleClassName());
        assertTrue(classDTOAntique.isInterface());
        assertEquals(0,classDTOAntique.getImplementedInterfaces().size());
        assertNull(classDTOAntique.getSuperClassName());

        //given
        ClassDTO classDTOAntiqueBuyableFahrrad = jshellReflection.getClassDTOs().get(1);
        //then
        assertEquals("AntiqueBuyableFahrrad",classDTOAntiqueBuyableFahrrad.getSimpleClassName());
        assertFalse(classDTOAntiqueBuyableFahrrad.isInterface());
        assertEquals(2,classDTOAntiqueBuyableFahrrad.getImplementedInterfaces().size());
        assertEquals("input.Antique",classDTOAntiqueBuyableFahrrad.getImplementedInterfaces().get(0));
        assertEquals("input.Buyable",classDTOAntiqueBuyableFahrrad.getImplementedInterfaces().get(1));
        assertEquals("input.Fahrrad",classDTOAntiqueBuyableFahrrad.getSuperClassName());

        //given
        ClassDTO classDTOFahrrad = jshellReflection.getClassDTOs().get(5);
        //then
        assertEquals("Fahrrad",classDTOFahrrad.getSimpleClassName());
        assertFalse(classDTOFahrrad.isInterface());
        assertEquals("input.Fahrzeug",classDTOFahrrad.getSuperClassName());
    }

    @Test
    void testClassToClassDTOFields(){
        ClassDTO fahrzeug = jshellReflection.getClassDTOs().get(6);

        assertEquals("private",fahrzeug.getFields().get(0).getModifier());
        assertEquals("double",fahrzeug.getFields().get(0).getType());
        assertEquals("speed",fahrzeug.getFields().get(0).getName());

        assertEquals("package",fahrzeug.getFields().get(1).getModifier());
        assertEquals("String",fahrzeug.getFields().get(1).getType());
        assertEquals("name",fahrzeug.getFields().get(1).getName());
    }

    @Test
    void testClassToClassDTOConstructors(){
        ClassDTO person = jshellReflection.getClassDTOs().get(8);

        assertEquals("Person",person.getConstructors().get(0).getName());

        assertEquals("public",person.getConstructors().get(0).getModifier());
        assertEquals(0,person.getConstructors().get(0).getInputParameters().size());

        assertEquals("package",person.getConstructors().get(1).getModifier());
        assertEquals(1,person.getConstructors().get(1).getInputParameters().size());

        assertEquals("private",person.getConstructors().get(2).getModifier());
        assertEquals(2,person.getConstructors().get(2).getInputParameters().size());

    }

    @Test
    void testClassToClassDTOMethods(){
        ClassDTO fahrzeug = jshellReflection.getClassDTOs().get(6);

        assertEquals("input.Fahrzeug",fahrzeug.getFullClassName());

        assertEquals(10,fahrzeug.getMethods().size());
        assertEquals("public",fahrzeug.getMethods().get(6).getModifier());
        assertEquals("void",fahrzeug.getMethods().get(6).getReturnType());
        assertEquals("setDieselTax",fahrzeug.getMethods().get(6).getName());
        assertEquals("double",fahrzeug.getMethods().get(6).getInputParameters().get(0));

    }

    @Test
    void testSerialize(){
        List<ClassDTO> classDTOs = jshellReflection.getClassDTOs();

        String classDTOsSerialized = jshellReflection.getClassDTOsSerialized();
        List<ClassDTO> deserialisiered = null;

        // deserialize the object
        try {
            assert classDTOsSerialized != null;
            byte [] data = Base64.getDecoder().decode( classDTOsSerialized );
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            deserialisiered  = (List<ClassDTO>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassDTO fahrzeug = classDTOs.get(6);
        ClassDTO fahrzeugDeserialized =  deserialisiered.get(6);


        assertEquals("input.Fahrzeug",fahrzeug.getFullClassName());
        assertEquals(fahrzeugDeserialized.getFullClassName(),fahrzeug.getFullClassName());

        assertEquals(4,fahrzeug.getFields().size());
        assertEquals(fahrzeugDeserialized.getFields().size(),fahrzeug.getFields().size());

        assertEquals("speed",fahrzeug.getFields().get(0).getName());
        assertEquals(fahrzeugDeserialized.getFields().get(0).getName(),fahrzeug.getFields().get(0).getName());

        assertEquals("public",fahrzeug.getConstructors().get(0).getModifier());
        assertEquals(fahrzeugDeserialized.getConstructors().get(0).getModifier(),fahrzeug.getConstructors().get(0).getModifier());

        assertEquals(10,fahrzeug.getMethods().size());
        assertEquals(fahrzeugDeserialized.getMethods().size(),fahrzeug.getMethods().size());

        assertEquals("setDieselTax",fahrzeug.getMethods().get(6).getName());
        assertEquals(fahrzeugDeserialized.getMethods().get(6).getName(),fahrzeug.getMethods().get(6).getName());
    }

    @Test
    void testSerializeFromJShell(){
        List<ClassDTO> classDTOs = jshellReflection.getClassDTOs();

        SnippetEvent snippetEvent = null;
        try {
            snippetEvent = jShellService.evaluateCode("jshellReflection.getClassDTOsSerialized();");
        } catch (InvalidCodeException e) {
            e.printStackTrace();
        }

        String serializedString = snippetEvent.value().substring(1,snippetEvent.value().length()-1);
        String classDTOsSerialized = serializedString;

        List<ClassDTO> deserialisiered = null;

        // deserialize the object
        try {
            byte [] data = Base64.getDecoder().decode( classDTOsSerialized );
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            deserialisiered  = (List<ClassDTO>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassDTO fahrzeug = classDTOs.get(6);
        ClassDTO fahrzeugDeserialized =  deserialisiered.get(6);


        assertEquals("input.Fahrzeug",fahrzeug.getFullClassName());
        assertEquals(fahrzeugDeserialized.getFullClassName(),fahrzeug.getFullClassName());

        assertEquals(4,fahrzeug.getFields().size());
        assertEquals(fahrzeugDeserialized.getFields().size(),fahrzeug.getFields().size());

        assertEquals("speed",fahrzeug.getFields().get(0).getName());
        assertEquals(fahrzeugDeserialized.getFields().get(0).getName(),fahrzeug.getFields().get(0).getName());

        assertEquals("public",fahrzeug.getConstructors().get(0).getModifier());
        assertEquals(fahrzeugDeserialized.getConstructors().get(0).getModifier(),fahrzeug.getConstructors().get(0).getModifier());

        assertEquals(10,fahrzeug.getMethods().size());
        assertEquals(fahrzeugDeserialized.getMethods().size(),fahrzeug.getMethods().size());

        assertEquals("setDieselTax",fahrzeug.getMethods().get(6).getName());
        assertEquals(fahrzeugDeserialized.getMethods().get(6).getName(),fahrzeug.getMethods().get(6).getName());
    }

}