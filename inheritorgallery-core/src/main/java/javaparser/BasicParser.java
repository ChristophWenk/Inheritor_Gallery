package javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BasicParser {
    String className = "";
    private static final String FILE_PATH = "inheritorgallery-core/src/main/java/jshell/workingClasses/Person.java";
            //"../inheritorgallery/core/SomeClass.java";

    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        parse(cu);
    }

    private static void parse(CompilationUnit cu) {
        NodeList<TypeDeclaration<?>> ltd = cu.getTypes();
        Node node = ltd.get(0); // assuming no nested classes
        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;

        System.out.println(coi.getName());
        System.out.println(coi.getFields());
        List<MethodDeclaration> md = coi.getMethods();
        for(MethodDeclaration m : md){
            // m.getAccessSpecifier()+ " " + m.getType() + " " + m.getName()+ " "
            System.out.println(m.getDeclarationAsString());
        }
    }



}
