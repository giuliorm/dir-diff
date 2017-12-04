package ru.juriasan.dirdiff.test;

import org.testng.Assert;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class EqualFilesWithEqualNamesTest extends BaseTest {

    File firstFile;
    File secondFile;
    private static final String NAME = "Equal Files With Different Names";
    private static final int FILE_COUNT = 2;

    public EqualFilesWithEqualNamesTest(String rootPath)  {
        super(NAME, rootPath);
    }

    @Override
    public void generateData() throws IOException {
        firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.getCanonicalPath(),
                "file1").toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectory.getCanonicalPath(),
                "file1").toString());
    }

    @Override
    public void checkData() throws IOException {
        if (resultDirectory == null)
            Assert.fail();
        FileService.assertExists(resultDirectory);
        FileService.assertDirectory(resultDirectory);
        File[] files = resultDirectory.listFiles();
        if (files == null)
            throw new RuntimeException();
        if (files.length != 0)
            Assert.fail();
    }
}
