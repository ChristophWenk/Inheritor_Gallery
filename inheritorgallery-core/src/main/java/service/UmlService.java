package service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import presentationmodel.uml.UmlPM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class UmlService {

    private static Logger logger = Logger.getLogger(UmlService.class.getName());


    public UmlPM createUmlPM() {

        List<CompilationUnit> cus = new ArrayList<>();
        // traverse all files in directory and subdirectories and create i list of CompilationUnit
        try {
            cus = Files.walk(Paths.get("../inheritorgallery-core/src/main/resources/uml"))
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(p -> new File(String.valueOf(p)))
                    .map(File::getAbsolutePath)
                    .map(this::getCompilationUnitFromFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //V0
        CompilationUnit cu = cus.get(9);

        //Version 1
        VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
        methodNameVisitor.visit(cu, null);


        //Version 2
        List<String> methodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        methodNameCollector.visit(cu, methodNames);
        methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));



        NodeList<TypeDeclaration<?>> ltd = cu.getTypes();
        Node node = ltd.get(0); // assuming no nested classes
        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;

        return new UmlPM(coi.getName().toString());

    }

    private CompilationUnit getCompilationUnitFromFile(String filepath){
        CompilationUnit cu = new CompilationUnit();
        File file = new File(filepath);
        try {
            cu = StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            // TODO Catch exception with useful error message
            e.printStackTrace();
        }
        return cu;
    }

    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found in " + fileName);
        } else {
            return new File(resource.getFile());
        }
    }


    private static class MethodNamePrinter extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);  //ensure child nodes of the current node are also visited
            System.out.println("Method Name Printed: " + md.getName());
        }
    }

    private static class MethodNameCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(MethodDeclaration md, List<String> collector) {
            super.visit(md, collector);
            collector.add(md.getNameAsString()); }
    }
}




