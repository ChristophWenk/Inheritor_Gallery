package service.jshell;

import com.google.common.reflect.TypeToken;
import com.headius.invokebinder.transform.Collect;
import exceptions.InvalidCodeException;
import input.Fahrzeug;
import input.Item;
import input.Person;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Singleton JShellService
 */
public class JShellService {
    private JShell jshell;

    private static Logger logger = LoggerFactory.getLogger(JShellService.class);

    private static JShellService jShellService;


    private JShellService() {
        jshell = JShell.create();
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        for (String cp : classpathEntries)
            jshell.addToClasspath(cp);
        jshell.eval("import input.*;");
    }

    public static JShellService getInstance() {
        if (jShellService == null) {
            jShellService = new JShellService();
        }
        return jShellService;
    }

    /**
     * @param input The code the JShell should execute
     * @return The output returned by JShell
     */
    public String processInput(String input) {

        if (input.contains("//") || input.contains("/*")) {
            //ToDo: implement cleansing mechanism
            return "Comments are not allowed as input. Remove comments and try again.";
        }
        List<SnippetEvent> snippetEventsList = jshell.eval(input);
        if (snippetEventsList.get(0).status().name().contains("REJECTED")) {
            //Todo: eventually throw exception
            //throw new InvalidCodeException("Code could not be interpreted by JShell. Please verify the statement.");
            return "Could not process input. Please verify the correctness of your statement.";
        } else {
            return snippetEventsList.get(0).value();

        }
    }

    public String getInstances() {
        Person p = new Person();

        Optional<Method> s = Arrays.stream(p.getClass().getMethods()).filter(c -> c.getName().equals("sayGreeting")).findFirst();
        if(s.isPresent()) {
            return s.get().getName();
        }
        return null;

    }

    public void testGetInstancesLocal(){
        Fahrzeug f = new Fahrzeug("tesla", 20);
        Item i = new Fahrzeug("tesla", 20);

        logger.info(getReferenceType(f));
        logger.info(getReferenceType(i));

        logger.info(f.toString()+" "+ f.getClass().getDeclaredMethods().length);
        logger.info(i.toString()+" "+ i.getClass().getDeclaredMethods().length);

        logger.info(f.toString()+" "+ f.getClass().getMethods().length);
        logger.info(i.toString()+" "+ i.getClass().getMethods().length);


        for(Method m : f.getClass().getDeclaredMethods()){
            logger.info(m.getName());
        }
    }


    public String getReferenceType(Item o){
        return ("Item");
    }
    public String getReferenceType(Fahrzeug o){
        return ("Fahrzeug");
    }




}
