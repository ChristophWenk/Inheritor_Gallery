
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import presentationmodel.FileChooserPM;
import service.FileService;
import service.instruction.AsciiDocService;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private static AsciiDocService asciiDocService;
    private static File file;


    @BeforeAll
    public static void setUp() {
        asciiDocService = new AsciiDocService();
        file = new File(FileServiceTest.class.getClassLoader().getResource("instructions/instructions.adoc").getFile());

    }

    @Test
    void loadFile(){
        asciiDocService.convertFile(file.toPath());
    }



}