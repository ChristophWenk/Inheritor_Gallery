package service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import presentationmodel.uml.UmlPM;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class UmlService {

    private static Logger logger = Logger.getLogger(UmlService.class.getName());
    //private static final String FILE_PATH = "inheritanceDummy/src/inheritanceDummyDemo/FahrzeugTest.java";
    // TODO Dynamic Path
    private static final String FILE_PATH = "inheritorgallery-core/src/main/java/jshell/workingClasses/Person.java";

    public UmlPM createUmlPM() {
        CompilationUnit cu = new CompilationUnit();
        cu = StaticJavaParser.parse(new String("public class Person {}"));
//        try {
//            cu = StaticJavaParser.parse(new File(FILE_PATH));
//
//        } catch (FileNotFoundException e) {
//            // TODO Catch exception with useful error message
//            e.printStackTrace();
//        }

        NodeList<TypeDeclaration<?>> ltd = cu.getTypes();
        Node node = ltd.get(0); // assuming no nested classes
        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;

        return new UmlPM(coi.getName().toString());

    }
}




