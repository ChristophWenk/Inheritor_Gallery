package service.instruction;

import org.asciidoctor.Asciidoctor;
import service.FileService;

import java.io.*;
import java.nio.file.Path;

import static org.asciidoctor.OptionsBuilder.options;
import static org.asciidoctor.jruby.internal.JRubyAsciidoctor.create;

public class AsciiDocService {

    private Asciidoctor asciidoctor;
    private FileService fileService;

    public AsciiDocService() {
        asciidoctor = create();
        fileService = new FileService();
    }

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
