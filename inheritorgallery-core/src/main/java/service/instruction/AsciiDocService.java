package service.instruction;

import org.asciidoctor.Asciidoctor;
import service.FileService;

import java.io.*;
import java.nio.file.Path;

import static org.asciidoctor.OptionsBuilder.options;
import static org.asciidoctor.jruby.internal.JRubyAsciidoctor.create;

/**
 * This class provides tools to convert .adoc files to HTML
 */
public class AsciiDocService {

    private Asciidoctor asciidoctor;
    private FileService fileService;

    /**
     * AsciiDocService Constructor
     */
    public AsciiDocService() {
        asciidoctor = create();
        fileService = new FileService();
    }

    /**
     * Convert a given .adoc file to HTML
     * @param file An .adoc file
     * @return HTML converted file as String
     */
    public String convertFile (String file) {
        FileReader reader = null;
        try {
            Path path = fileService.getPath(file);
            reader = new FileReader(new File(fileService.getPathAsString(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringWriter writer = new StringWriter();

        try {
            asciidoctor.convert(reader, writer, options().asMap());
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer htmlBuffer = writer.getBuffer();
        return htmlBuffer.toString();
    }


}
