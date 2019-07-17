
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.FileService;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private static FileService fileService;

    @BeforeAll
    public static void setUp() {
        fileService = new FileService();
    }

    @Test
    void testGetPath(){
        Path path = fileService.getPath("/input");
        assertEquals("C:\\Users\\Dimitri\\IdeaProjects\\InheritorGallery\\inheritorgallery-core\\out\\production\\classes\\input",
                path.toString());



    }

}