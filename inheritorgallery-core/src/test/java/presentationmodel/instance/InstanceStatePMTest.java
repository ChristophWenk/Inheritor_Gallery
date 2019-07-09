package presentationmodel.instance;

import exceptions.InvalidCodeException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.jshell.JShellService;
import service.jshell.ObjectDTO;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstanceStatePMTest {
    private static InstanceStatePM instanceStatePM;
    private static JShellService jShellService = JShellService.getInstance();

    @BeforeAll
    public static void setUp() {
        instanceStatePM = new InstanceStatePM();
    }

    @BeforeEach
    public void resetJShell() {
        jShellService.reset();
    }

    @Test
    void getObjectPMsTest(){
        //given

        //when
        instanceStatePM.setJShellInput("Person p1 = new Person();");

        //then
        assertEquals(1,instanceStatePM.getObjectPMs().size());
        assertEquals("Person",instanceStatePM.getObjectPMs().get(0).getObjectName());

        //when
        instanceStatePM.setJShellInput("Person p2 = new Person();");

        //then
        assertEquals(2,instanceStatePM.getObjectPMs().size());
    }

    @Test
    void getObjectPMsTestAddSameObject(){
        //given

        //when
        instanceStatePM.setJShellInput("Person p1 = new Person();");
        instanceStatePM.setJShellInput("Person p1 = new Person();");
        instanceStatePM.setJShellInput("Person p1 = new Person();");

        //then
        assertEquals(1,instanceStatePM.getObjectPMs().size());
        assertEquals("Person",instanceStatePM.getObjectPMs().get(0).getObjectName());

    }

}
