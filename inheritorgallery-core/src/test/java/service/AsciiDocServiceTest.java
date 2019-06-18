package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsciiDocServiceTest {

    private AsciiDocService asciiDocService = new AsciiDocService();

    @Test
    void testConvertFile() {
        assertEquals("<div class=\"paragraph\">\n" +
                "<p>Writing AsciiDoc is <em>easy</em>!</p>\n" +
                "</div>", asciiDocService.convertFile("/instructions/asciiDocTest.adoc"));
    }
}