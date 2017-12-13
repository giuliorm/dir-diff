package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import ru.juriasan.services.FileService;

public class EqualFilesWithDifferentNamesTest extends BaseTest {

    Path firstFile;
    Path secondFile;
    private static final String NAME = "Equal Files With Different Names";

    public EqualFilesWithDifferentNamesTest(String rootPath)  {
        super(NAME, rootPath);
    }

    @Override
    public void generateData() throws IOException {
        firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.toRealPath().toString(),
                "file1").toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectory.toRealPath().toString(),
                "file2").toString());
    }

    @Override
    public void checkData() throws IOException {
       checkDifferentFiles(firstFile, secondFile);
    }
}
