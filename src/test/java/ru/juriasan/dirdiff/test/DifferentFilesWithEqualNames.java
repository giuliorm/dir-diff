package ru.juriasan.dirdiff.test;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import ru.juriasan.services.FileService;
import ru.juriasan.util.NewFilenameManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class DifferentFilesWithEqualNames  extends BaseTest {

    File firstFile;
    File secondFile;
    private static final int FILE_COUNT = 2;

    public DifferentFilesWithEqualNames(String rootPath)  {
        super(rootPath);
    }

    @Override
    public void generateData() throws IOException {
        firstFile = FileService.getPlainFileManager().create(Paths.get(rootDirectoryPath, "file1").toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(rootDirectoryPath, "file1").toString());
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
        String firstName = firstFile.getName();
        String secondName = firstFile.getName();
        if (new NewFilenameManager(firstName).getFileNumber(firstName) <= 0 && new NewFilenameManager(secondName)
                .getFileNumber(firstName) <= 0)
            Assert.fail();
        if (!FileUtils.contentEquals(firstFile, this.firstFile) || !FileUtils.contentEquals(secondFile,
                this.secondFile))
            Assert.fail();
    }
}
