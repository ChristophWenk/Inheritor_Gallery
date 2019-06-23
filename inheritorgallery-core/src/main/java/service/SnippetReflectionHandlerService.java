package service;

import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;

import java.lang.reflect.Method;

/**
 *
 */
public class SnippetReflectionHandlerService {

    private Method m[];
    private JShellService jShellService = JShellService.getInstance();

    public SnippetReflectionHandlerService() {
    }

    public String getClassName(SnippetEvent snippetEvent) {
        VarSnippet snippet = (VarSnippet) snippetEvent.snippet();
        String className = snippet.typeName();

        return className;
    }

    public String getMethods(SnippetEvent snippetEvent) {
        VarSnippet snippet = (VarSnippet) snippetEvent.snippet();
        String className = snippet.typeName();

        String refName = snippet.name();
        //jShellService.evaluateCode(className + " anyClass = " + "new " + className + "();");

       //String pkgName = jShellService.evaluateCode(refName + ".getClass().getPackage();");
        String pkgName = jShellService.evaluateCode("p.getFirstName();");

        try {


            Class cls = Class.forName("input."+className);
            m = cls.getMethods();
        }
        catch (Exception e) {
        }
        return null;
    }
}
