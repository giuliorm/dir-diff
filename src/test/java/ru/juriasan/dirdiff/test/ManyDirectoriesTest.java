package ru.juriasan.dirdiff.test;


import org.testng.annotations.Test;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class ManyDirectoriesTest extends BaseTest {

    public ManyDirectoriesTest(String rootPath) {
        super(rootPath);
    }

    private void generateManyDirectories(int number, File first, File second) throws IOException {
        if (number <= 0 || first == null || second == null || !first.isDirectory() || !second.isDirectory())
            throw new RuntimeException();
        for (int i = 0; i < number; i++) {
            String dirName = String.format("dir %d", i);
            String fileName = String.format("file %d", i);
            File firstDir = FileService.getDirectoryManager().create(
                   Paths.get(first.getCanonicalPath(), dirName).toString());
            File secondDir = FileService.getDirectoryManager().create(
                    Paths.get(second.getCanonicalPath(), dirName).toString());
            FileService.getPlainFileManager().create(Paths.get(firstDir.getCanonicalPath(), fileName).toString());
            File file = FileService.getPlainFileManager().create(Paths.get(secondDir.getCanonicalPath(), fileName)
                    .toString());
            try (FileWriter w = new FileWriter(file)) {
                w.write("HW!");
            }
        }
    }

    @Override
    public void generateData() throws IOException {
        generateManyDirectories(100, firstDirectory, secondDirectory);
    }

    @Override
    public void checkData() throws IOException {

    }
}
