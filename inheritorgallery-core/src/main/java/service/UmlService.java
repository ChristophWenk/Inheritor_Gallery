package service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class UmlService {

    private static Logger logger = Logger.getLogger(UmlService.class.getName());
    private List<ClassDTO> classDTOs;
    private List<EdgeDTO> edgeDTOs;

    public UmlService(){
        FileService fileService = new FileService();
        Path path = fileService.getPath("/uml");

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

        classDTOs =  classDeclarations.stream().map(this::classDeclarationToClassDTO).collect(Collectors.toList());
        edgeDTOs = searchEdges(classDeclarations);
    }



    public List<ClassDTO> getClassDTOs() {
        return classDTOs;
    }
    public List<EdgeDTO> getEdgeDTOs() {
        return edgeDTOs;
    }

    private ClassDTO classDeclarationToClassDTO(ClassOrInterfaceDeclaration classDeclaration){
        String className = classDeclaration.getNameAsString();
        //String fullyQualifiedClassName = classDeclaration.getFullyQualifiedName().isPresent() ?
        //        classDeclaration.getFullyQualifiedName().get() : className ;

        //fields
        List<String> fields = classDeclaration.getFields().stream()
                .map(Node::toString)
                .collect(Collectors.toList());
        //constructors
        List<String> constructors = classDeclaration.getConstructors().stream()
                .map(NodeWithDeclaration::getDeclarationAsString)
                .collect(Collectors.toList());
        //methods
        List<String> methods = classDeclaration.getMethods().stream()
                .map(NodeWithDeclaration::getDeclarationAsString)
                .collect(Collectors.toList());


        return new ClassDTO(className, fields, constructors, methods);
    }

    private List<EdgeDTO> searchEdges(List<ClassOrInterfaceDeclaration> classDeclarations){

        List<EdgeDTO> edgeDTOS = new ArrayList<>();

        for(ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
            String className = classDeclaration.getNameAsString();

            List<String> extendsDeclaration = classDeclaration.getExtendedTypes().stream()
                    .map(c -> c.getName().asString())
                    .collect(Collectors.toList());
            List<String> implementsDeclaration = classDeclaration.getImplementedTypes().stream()
                    .map(c -> c.getName().asString())
                    .collect(Collectors.toList());

            for (String s : extendsDeclaration) {
                edgeDTOS.add(new EdgeDTO(className,s,"extends"));
            }
            for (String s : implementsDeclaration) {
                edgeDTOS.add(new EdgeDTO(className,s,"implements"));
            }
        }

        return edgeDTOS;
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




