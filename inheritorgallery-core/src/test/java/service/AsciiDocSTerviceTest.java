package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsciiDocSTerviceTest {

    private AsciiDocService asciiDocService = new AsciiDocService();

    @Test
    void testConvertFile() {
        assertEquals("Writing AsciiDoc is _easy_!",asciiDocService.convertFile("src/main/resources/instructions/instructions.adoc"));
    }
}