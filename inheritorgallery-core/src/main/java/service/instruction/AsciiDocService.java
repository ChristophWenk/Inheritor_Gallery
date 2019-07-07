package service.instruction;

import org.asciidoctor.Asciidoctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;

import java.io.*;
import java.nio.file.Path;

import static org.asciidoctor.OptionsBuilder.options;
import static org.asciidoctor.jruby.internal.JRubyAsciidoctor.create;

/**
 * This class provides tools to convert .adoc files to HTML
 */
public class AsciiDocService {
    private static Logger logger = LoggerFactory.getLogger(AsciiDocService.class);

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
     * @param filepath A path to an .adoc file
     * @return HTML converted file as String
     */
    public String convertFile (String filepath) {
        FileReader reader = null;
        try {
            Path path = fileService.getPath(filepath);
            reader = new FileReader(new File(fileService.getPathAsString(path)));
        } catch (FileNotFoundException e) {
            logger.error("File path not valid: " + filepath, e);
        }
        StringWriter writer = new StringWriter();

        try {
            asciidoctor.convert(reader, writer, options().asMap());
        } catch (IOException e) {
            logger.error("Could not convert .adoc file: " + filepath, e);
        }

        StringBuffer htmlBuffer = writer.getBuffer();
        return htmlBuffer.toString();
    }


}
