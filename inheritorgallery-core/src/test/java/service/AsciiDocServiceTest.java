package service;

import org.junit.jupiter.api.Test;
import service.instruction.AsciiDocService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsciiDocServiceTest {

    private AsciiDocService asciiDocService = new AsciiDocService();

    @Test
    void testConvertFile() {
        //given
        String expected = "<div class=\"paragraph\">\n" +
                "<p>Writing AsciiDoc is <em>easy</em>!</p>\n" +
                "</div>";

        //when, then
        assertEquals(expected, asciiDocService.convertFile("/instructions/asciiDocTest.adoc"));
    }
}