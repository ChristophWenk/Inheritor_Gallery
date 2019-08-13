package service.instruction;

import org.asciidoctor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;

import static org.asciidoctor.jruby.internal.JRubyAsciidoctor.create;

/**
 * This class provides tools to convert .adoc files to HTML
 */
public class AsciiDocService {
    private static Logger logger = LoggerFactory.getLogger(AsciiDocService.class);

    private Asciidoctor asciidoctor;
    private FileService fileService;

    private String filterStart = "<div id=\"footer\">\n" +
            "<div id=\"footer-text\">\n" +
            "Last updated";
    private String filterEnd = "</div>\n" +
            "</div>";

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

        AttributesBuilder attributes = AttributesBuilder.attributes();
        OptionsBuilder options = OptionsBuilder.options();
        options.safe(SafeMode.UNSAFE);
        options.attributes(attributes);
        // headerFooter must be set to true in order to display the content with CSS
        // unfortunately this also adds a timestamp to the output, which must be replaced again
        options.headerFooter(true);
        options.backend("html5");

        try {
            asciidoctor.convert(reader, writer, options);
        } catch (IOException e) {
            logger.error("Could not convert .adoc file: " + filepath, e);
        }

        StringBuffer htmlBuffer = writer.getBuffer();

        if (htmlBuffer.toString().contains(filterStart)) {
            String output = htmlBuffer.toString().replaceAll(filterStart + ".*", "");
            logger.debug(output);
            return output;
        }
        return htmlBuffer.toString();
    }


}
