package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import org.testng.Assert;
import ru.juriasan.services.FileService;

public class ManyDirectoriesTest extends BaseTest {

    private static final String NAME = "Many Directories test";
    private int directoriesCount = 1000;
    private static final int MAX_DIRECTORIES = 10000;

    public ManyDirectoriesTest(int directoriesCount, String rootPath) {
        super(NAME, rootPath);
        this.directoriesCount = directoriesCount  > 0 && directoriesCount < MAX_DIRECTORIES ? directoriesCount :
                MAX_DIRECTORIES;
    }

    @Override
    public void generateData() throws IOException {
        if (directoriesCount <= 0 || firstDirectory == null || secondDirectory == null
                || !Files.isDirectory(firstDirectory) || !Files.isDirectory(secondDirectory))
            throw new RuntimeException();
        for (int i = 0; i < directoriesCount; i++) {
            String dirName = String.format("dir %d", i);
            String fileName = String.format("file %d", i);
            Path firstDir = FileService.getDirectoryManager().create(
                    Paths.get(firstDirectory.toRealPath().toString(), dirName).toString());
            Path secondDir = FileService.getDirectoryManager().create(
                    Paths.get(secondDirectory.toRealPath().toString(), dirName).toString());
            FileService.getPlainFileManager().create(Paths.get(firstDir.toRealPath().toString(), fileName).toString());
            Path file = FileService.getPlainFileManager().create(Paths.get(secondDir.toRealPath().toString(), fileName)
                    .toString());
            FileService.write(file, "HW!");
        }
    }

    @Override
    public void checkData() throws IOException {
        if (resultDirectory == null || !Files.isDirectory(resultDirectory))
            Assert.fail();

        Collection<Path> files = FileService.getDirectoryManager().getFiles(resultDirectory);
        if (files == null || files.size() != directoriesCount)
            Assert.fail();
        files.forEach(file -> {
            try {
                FileService.assertExists(file);
                FileService.assertDirectory(file);
            }
            catch (IOException ex) {
                Assert.fail();
            }
        });
    }
}
