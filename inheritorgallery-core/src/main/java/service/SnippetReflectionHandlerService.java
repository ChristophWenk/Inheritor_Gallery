package service;

import exceptions.InvalidCodeException;
import jdk.jshell.SnippetEvent;
import jdk.jshell.VarSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

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

    public String getMethods(SnippetEvent snippetEvent) {
        VarSnippet snippet = (VarSnippet) snippetEvent.snippet();
        String className = snippet.typeName();
        String packageName = getPackage(snippetEvent);

        try {
            Class cls = Class.forName(packageName + "." + className);
            Method m[] = cls.getMethods();
        }
        catch (Exception e) {
        }
        return null;
    }

    private String getPackage(SnippetEvent snippetEvent) {
        VarSnippet snippet = (VarSnippet) snippetEvent.snippet();
        String referenceName = snippet.name();
        String pkg = "";

        try {
            pkg = jShellService.evaluateCode(referenceName + ".getClass().getPackage();");
        } catch (InvalidCodeException e) {
            logger.error("Invalid code: " + snippetEvent.snippet().source());
            e.printStackTrace();
        }
        String[] pkgNameParts = pkg.split(" ");
        String pkgName = pkgNameParts[1];
        return pkgName;
    }
}
