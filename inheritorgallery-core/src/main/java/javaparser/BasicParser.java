package javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class BasicParser {

    private static Logger logger = Logger.getLogger(BasicParser.class.getName());
    //private static final String FILE_PATH = "inheritanceDummy/src/inheritanceDummyDemo/FahrzeugTest.java";
    private static final String FILE_PATH = "inheritorgallery-core/src/main/java/jshell/workingClasses/Person.java";

    public static void main(String[] args) throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        parse(cu);

    }

    private static void parse(CompilationUnit cu) {

        NodeList<TypeDeclaration<?>> ltd = cu.getTypes();
        Node node = ltd.get(0); // assuming no nested classes
        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;

        List<FieldDeclaration> fields = coi.getFields();
        logger.info(String.valueOf(fields));

        List<String> methods = coi.getMethods().stream()
                .map(
                        m -> m.getAccessSpecifier() + " " + m.getType() + " " + m.getName() + " " + m.getParameters())
                .collect(Collectors.toList());
        logger.info(String.valueOf(methods));

    }
}




