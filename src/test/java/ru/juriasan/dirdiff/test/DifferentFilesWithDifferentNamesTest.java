package ru.juriasan.dirdiff.test;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class DifferentFilesWithDifferentNamesTest extends BaseTest {

    File firstFile;
    File secondFile;
    private static final String NAME = "Different Files with Different Names";
    private static final int FILE_COUNT = 2;

    public DifferentFilesWithDifferentNamesTest(String rootPath) {
        super(NAME, rootPath);
    }

    @Override
    public void generateData() throws IOException {
        firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.getCanonicalPath(),
                "file1").toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectory.getCanonicalPath(),
                "file2").toString());
        try(FileWriter w = new FileWriter(secondFile)) {
            w.write("HW!");
        }
    }

    @Override
    public void checkData() throws IOException {
       checkDifferentFiles(firstFile, secondFile);
    }
}
