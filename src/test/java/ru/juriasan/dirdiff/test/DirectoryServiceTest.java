package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.annotations.Test;
import org.testng.Assert;
import ru.juriasan.services.FileService;

public class DirectoryServiceTest {

    private static final String resourcesPath = Paths.get("src", "test", "resources").toString();

    @Test
    public void createTestStringPath() throws IOException {
        String path = Paths.get(resourcesPath, "createTest").toString();
        Path result = FileService.getDirectoryManager().create(path);
        if (result == null)
            Assert.fail();
        FileService.assertDirectory(result);
        FileService.assertExists(result);
        FileService.assertReadable(result);
        FileService.assertExecutable(result);
        Files.delete(result);
    }

    @Test
    public void createTestFile() throws IOException {

        Path result = Paths.get(resourcesPath, "createTest");
        FileService.getDirectoryManager().create(result);
        FileService.assertDirectory(result);
        FileService.assertExists(result);
        FileService.assertReadable(result);
        FileService.assertExecutable(result);
        Files.delete(result);
    }

}
