package service.jshell;

import exceptions.InvalidCodeException;
import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class SnippetReflectionHandlerService {

    private static Logger logger = LoggerFactory.getLogger(SnippetReflectionHandlerService.class);
    private JShellService jShellService = JShellService.getInstance();

    public SnippetReflectionHandlerService() {
    }

    public String getClassName(SnippetEvent snippetEvent) {
        VarSnippet snippet = (VarSnippet) snippetEvent.snippet();
        String className = snippet.typeName();

        return className;
    }

//    public List<Method> getClassMethods(SnippetEvent snippetEvent) {
//        VarSnippet snippet = (VarSnippet) snippetEvent.snippet();
//        String className = snippet.typeName();
//        String packageName = getPackage(snippetEvent);
//        Method methods[] = null;
//
//        try {
//            Class cls = Class.forName(packageName + "." + className);
//            methods = cls.getMethods();
//        }
//        catch (Exception e) {
//        }
//        return filterMethods(methods,packageName);
//    }

    /**
     * Filter a method array so that it only contains inherited methods from classes within the same package.
     * These are the only ones that are of interest for a user.
     * @param methods The methods to be filtered
     * @param pkg The target package that is the filter
     * @return a list of filtered methods according to the input package
     */
    private List<Method> filterMethods(Method[] methods, String pkg) {
        ArrayList<Method> arrayList = new ArrayList<Method>(Arrays.asList(methods));
        return arrayList.stream()
        .filter(method -> method.toString().contains(pkg))
        .collect(Collectors.toList());
    }
}
