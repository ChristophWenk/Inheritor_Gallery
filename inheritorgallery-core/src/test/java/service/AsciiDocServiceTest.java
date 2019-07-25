package service;

import org.junit.jupiter.api.Test;
import service.instruction.AsciiDocService;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AsciiDocServiceTest {

    private AsciiDocService asciiDocService = new AsciiDocService();

    @Test
    void testConvertFile() {
        //given
        String expected = "<div class=\"paragraph\">\n" +
                "<p>Writing AsciiDoc is <em>easy</em>!</p>\n" +
                "</div>";
        FileService fileService = new FileService();
        Path path = fileService.getPath("/instructions/asciiDocTest.adoc");

        //when, then
        boolean outcome = asciiDocService.convertFile(path).contains(expected);
        assertTrue(outcome);
    }
}