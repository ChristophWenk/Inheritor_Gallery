package service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import presentationmodel.uml.UmlPM;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.logging.Logger;


public class UmlService {

    private static Logger logger = Logger.getLogger(UmlService.class.getName());
    //private static final String FILE_PATH = "inheritanceDummy/src/inheritanceDummyDemo/FahrzeugTest.java";
    // TODO Dynamic Path

    //private static final String FILE_PATH = "inheritorgallery-core/src/main/java/jshell/workingClasses/Person.java";
    private static final String FILE_PATH = "uml/Person.java";

    public UmlPM createUmlPM() {
        CompilationUnit cu = new CompilationUnit();
        File file = getFileFromResources(FILE_PATH);
        try {
            cu = StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            // TODO Catch exception with useful error message
            e.printStackTrace();
        }

        NodeList<TypeDeclaration<?>> ltd = cu.getTypes();
        Node node = ltd.get(0); // assuming no nested classes
        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;

        return new UmlPM(coi.getName().toString());

    }

    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
}




