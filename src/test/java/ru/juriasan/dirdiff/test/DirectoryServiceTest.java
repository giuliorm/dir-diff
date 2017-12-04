package ru.juriasan.dirdiff.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class DirectoryServiceTest {

    private static final String resourcesPath = Paths.get("src", "test", "resources").toString();

    @Test
    public void createTestStringPath() throws IOException {
        String path = Paths.get(resourcesPath, "createTest").toString();
        File result = FileService.getDirectoryManager().create(path);
        if (result == null)
            Assert.fail();
        FileService.assertDirectory(result);
        FileService.assertExists(result);
        FileService.assertReadable(result);
        FileService.assertExecutable(result);
        result.delete();
    }

    @Test
    public void createTestFile() throws IOException {

        File result = new File( Paths.get(resourcesPath, "createTest").toString());
        FileService.getDirectoryManager().create(result);
        FileService.assertDirectory(result);
        FileService.assertExists(result);
        FileService.assertReadable(result);
        FileService.assertExecutable(result);
        result.delete();
    }

}
