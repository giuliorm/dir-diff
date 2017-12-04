package ru.juriasan.dirdiff.test;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class DifferentFilesWithDifferentNames extends BaseTest {

    File firstFile;
    File secondFile;
    private static final String NAME = "Different Files with Different Names";
    private static final int FILE_COUNT = 2;

    public DifferentFilesWithDifferentNames(String rootPath) {
        super(NAME, rootPath);
    }

    @Override
    public void generateData() throws IOException {
        firstFile = FileService.getPlainFileManager().create(Paths.get(rootDirectoryPath, "file1").toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(rootDirectoryPath, "file2").toString());
        try(FileWriter w = new FileWriter(secondFile)) {
            w.write("HW!");
        }
    }

    @Override
    public void checkData() throws IOException {
        if (resultDirectory == null)
            Assert.fail();
        File[] result = resultDirectory.listFiles();
        if (result == null)
            throw new RuntimeException();
        if (result.length != FILE_COUNT)
            Assert.fail();
        File firstFile = result[0];
        File secondFile = result [1];
        if (!FileUtils.contentEquals(firstFile, this.firstFile) || !FileUtils.contentEquals(secondFile,
                this.secondFile))
            Assert.fail();
    }
}
