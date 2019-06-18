package service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class UmlService {

    private static Logger logger = Logger.getLogger(UmlService.class.getName());

    public List<ClassDTO> getClasses() {
        FileService fileService = new FileService();
        Path path = fileService.getPath("../uml");


        List<CompilationUnit> cus = new ArrayList<>();
        // traverse all files in directory and subdirectories and create i list of CompilationUnit
        try {
            cus = Files.walk(path)
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(p -> new File(String.valueOf(p)))
                    .map(File::getAbsolutePath)
                    .map(this::getCompilationUnitFromFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create classes from CompilationUnits
        List<ClassOrInterfaceDeclaration> classDeclarations = new ArrayList<>();
        VoidVisitor<List<ClassOrInterfaceDeclaration>> classCollector = new ClassCollector();
        for (CompilationUnit cu : cus) {
            classCollector.visit(cu, classDeclarations);
        }

        return classDeclarations.stream().map(c -> getClassDTO(c)).collect(Collectors.toList());

    }

    public ClassDTO getClassDTO(ClassOrInterfaceDeclaration classDeclaration){
        List<String> fields = classDeclaration.getFields().stream()
                .map(c -> c.toString())
                .collect(Collectors.toList());

        List<String> constructors = classDeclaration.getConstructors().stream()
                .map(c -> c.getDeclarationAsString())
                .collect(Collectors.toList());

        List<String> methods = classDeclaration.getMethods().stream()
                .map(c -> c.getDeclarationAsString())
                .collect(Collectors.toList());

        return new ClassDTO(classDeclaration.getNameAsString(), fields, constructors, methods);
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

    private static class ClassCollector extends VoidVisitorAdapter<List<ClassOrInterfaceDeclaration>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cd, List<ClassOrInterfaceDeclaration> collector) {
            super.visit(cd, collector);
            collector.add(cd); }
    }
}




