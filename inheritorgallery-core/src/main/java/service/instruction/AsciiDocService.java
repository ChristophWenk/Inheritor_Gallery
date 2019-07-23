package service.instruction;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;

import static org.asciidoctor.Asciidoctor.Factory.create;

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
    public String convertFile (Path filepath) {
        logger.info("loading adoc " + filepath);
        FileReader reader = null;
        try {
            reader = new FileReader(filepath.toFile());
            logger.info(reader.toString());
        } catch (FileNotFoundException e) {
            logger.error("File path not valid: " + filepath, e);
        }
        StringWriter writer = new StringWriter();

        try {
            asciidoctor.convert(reader, writer, OptionsBuilder.options().asMap());
        } catch (IOException e) {
            logger.error("Could not convert .adoc file: " + filepath, e);
        }

        StringBuffer htmlBuffer = writer.getBuffer();
        return htmlBuffer.toString();
    }


}
