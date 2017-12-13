package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import org.testng.Assert;
import ru.juriasan.services.FileService;

public class EqualFilesWithEqualNamesTest extends BaseTest {

    Path firstFile;
    Path secondFile;
    private static final String NAME = "Equal Files With Different Names";
    private static final int FILE_COUNT = 2;

    public EqualFilesWithEqualNamesTest(String rootPath)  {
        super(NAME, rootPath);
    }

    @Override
    public void generateData() throws IOException {
        firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.toRealPath().toString(),
                "file1").toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectory.toRealPath().toString(),
                "file1").toString());
    }

    @Override
    public void checkData() throws IOException {
        if (resultDirectory == null)
            Assert.fail();
        FileService.assertExists(resultDirectory);
        FileService.assertDirectory(resultDirectory);
        Collection<Path> result = FileService.getDirectoryManager().getFiles(resultDirectory);
        if (result.size() != 0)
            Assert.fail();
    }
}
