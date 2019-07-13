package service.uml;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.FileService;
import service.jshell.dto.ClassDTO;
import jshellExtensions.JShellReflection;

import java.io.*;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JShellReflectionTest {
    private static JShellReflection JShellReflection;
    private static Path path;

    @BeforeAll
    public static void setUp() {
        JShellReflection = new JShellReflection();
        FileService fileService = new FileService();
        path = fileService.getPath("/input");
    }

    @Test
    void testGetClassesForPath(){
        //given
        List<Class> classes = JShellReflection.getClassesForPath(path);

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
        ClassDTO classDTOAntique = JShellReflection.getClassDTOs().get(0);
        //then
        assertEquals("input.Antique",classDTOAntique.getFullClassName());
        assertEquals("Antique",classDTOAntique.getSimpleClassName());
        assertTrue(classDTOAntique.isInterface());
        assertEquals(0,classDTOAntique.getImplementedInterfaces().size());
        assertNull(classDTOAntique.getSuperClassName());

        //given
        ClassDTO classDTOAntiqueBuyableFahrrad = JShellReflection.getClassDTOs().get(1);
        //then
        assertEquals("AntiqueBuyableFahrrad",classDTOAntiqueBuyableFahrrad.getSimpleClassName());
        assertFalse(classDTOAntiqueBuyableFahrrad.isInterface());
        assertEquals(2,classDTOAntiqueBuyableFahrrad.getImplementedInterfaces().size());
        assertEquals("input.Antique",classDTOAntiqueBuyableFahrrad.getImplementedInterfaces().get(0));
        assertEquals("input.Buyable",classDTOAntiqueBuyableFahrrad.getImplementedInterfaces().get(1));
        assertEquals("input.Fahrrad",classDTOAntiqueBuyableFahrrad.getSuperClassName());

        //given
        ClassDTO classDTOFahrrad = JShellReflection.getClassDTOs().get(5);
        //then
        assertEquals("Fahrrad",classDTOFahrrad.getSimpleClassName());
        assertFalse(classDTOFahrrad.isInterface());
        assertEquals("input.Fahrzeug",classDTOFahrrad.getSuperClassName());
    }

    @Test
    void testClassToClassDTOFields(){
        ClassDTO fahrzeug = JShellReflection.getClassDTOs().get(6);

        assertEquals("private",fahrzeug.getFields().get(0).getModifier());
        assertEquals("double",fahrzeug.getFields().get(0).getType());
        assertEquals("speed",fahrzeug.getFields().get(0).getName());

        assertEquals("package",fahrzeug.getFields().get(1).getModifier());
        assertEquals("String",fahrzeug.getFields().get(1).getType());
        assertEquals("name",fahrzeug.getFields().get(1).getName());
    }

    @Test
    void testClassToClassDTOConstructors(){
        ClassDTO person = JShellReflection.getClassDTOs().get(8);

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
        ClassDTO fahrzeug = JShellReflection.getClassDTOs().get(6);

        assertEquals("input.Fahrzeug",fahrzeug.getFullClassName());

        assertEquals(10,fahrzeug.getMethods().size());
        assertEquals("public",fahrzeug.getMethods().get(6).getModifier());
        assertEquals("void",fahrzeug.getMethods().get(6).getReturnType());
        assertEquals("setDieselTax",fahrzeug.getMethods().get(6).getName());
        assertEquals("double",fahrzeug.getMethods().get(6).getInputParameters().get(0));

    }

    @Test
    void testSerialize(){
        ClassDTO fahrzeug = JShellReflection.getClassDTOs().get(6);

        String serialiedString = null;
        ClassDTO fahrzeugDeserialized = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(byteArrayOutputStream);
            o.writeObject(fahrzeug);
            o.flush();
            o.close();
            serialiedString =  Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // deserialize the object
        try {
            assert serialiedString != null;
            byte [] data = Base64.getDecoder().decode( serialiedString );
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            fahrzeugDeserialized  = (ClassDTO) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

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