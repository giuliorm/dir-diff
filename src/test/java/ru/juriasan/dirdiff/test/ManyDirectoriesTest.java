package ru.juriasan.dirdiff.test;


import org.testng.Assert;
import org.testng.annotations.Test;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

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
                || !firstDirectory.isDirectory() || !secondDirectory.isDirectory())
            throw new RuntimeException();
        for (int i = 0; i < directoriesCount; i++) {
            String dirName = String.format("dir %d", i);
            String fileName = String.format("file %d", i);
            File firstDir = FileService.getDirectoryManager().create(
                    Paths.get(firstDirectory.getCanonicalPath(), dirName).toString());
            File secondDir = FileService.getDirectoryManager().create(
                    Paths.get(secondDirectory.getCanonicalPath(), dirName).toString());
            FileService.getPlainFileManager().create(Paths.get(firstDir.getCanonicalPath(), fileName).toString());
            File file = FileService.getPlainFileManager().create(Paths.get(secondDir.getCanonicalPath(), fileName)
                    .toString());
            try (FileWriter w = new FileWriter(file)) {
                w.write("HW!");
            }
        }
    }

    @Override
    public void checkData() throws IOException {
        if (resultDirectory == null || !resultDirectory.isDirectory())
            Assert.fail();

        File[] files = resultDirectory.listFiles();
        if (files == null || files.length != directoriesCount)
            Assert.fail();

        for (int i = 0; i < directoriesCount; i++) {
            File current = files[i];
            FileService.assertExists(current);
            FileService.assertDirectory(current);
        }
    }
}
