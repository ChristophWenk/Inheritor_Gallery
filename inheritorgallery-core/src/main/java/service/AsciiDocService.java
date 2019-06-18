package service;

import org.asciidoctor.Asciidoctor;

import java.io.*;

import static org.asciidoctor.OptionsBuilder.options;
import static org.asciidoctor.jruby.internal.JRubyAsciidoctor.create;

public class AsciiDocService {

    Asciidoctor asciidoctor;

    public AsciiDocService() {
        asciidoctor = create();
    }

    public String convertFile (String file) {
        FileReader reader = null;
        try {
            reader = new FileReader(new File(file));
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
