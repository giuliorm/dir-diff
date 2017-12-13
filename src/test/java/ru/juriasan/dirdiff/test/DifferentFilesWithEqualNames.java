package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import ru.juriasan.services.FileService;
import ru.juriasan.util.NewFilenameManager;

public class DifferentFilesWithEqualNames  extends BaseTest {

    Path firstFile;
    Path secondFile;
    private static final String NAME = "Different Files With Equal Names";
    private static final int FILE_COUNT = 2;

    public DifferentFilesWithEqualNames(String rootPath)  {
        super(NAME, rootPath);
    }

    @Override
    public void generateData() throws IOException {
        firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.toRealPath().toString(),
                "file1").toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectory.toRealPath().toString(),
                "file1").toString());
        FileService.write(secondFile, "HW!");
    }

    @Override
    public void checkData() throws IOException {
        if (resultDirectory == null)
            Assert.fail();
        List<Path> result = new ArrayList<>(FileService.getDirectoryManager().getFiles(resultDirectory));
        if (result.size() != FILE_COUNT)
            Assert.fail();
        Path firstFile = result.get(0);
        Path secondFile = result.get(1);
        String firstName = firstFile.getFileName().toString();
        String secondName = secondFile.getFileName().toString();
        String initialName = getInitialName(firstName, secondName);
        if (initialName == null)
            Assert.fail();
        NewFilenameManager manager =  new NewFilenameManager(initialName);
        checkNames(manager, firstName, secondName);

        if (!FileService.contentEquals(firstFile, this.firstFile) || !FileService.contentEquals(secondFile,
                this.secondFile))
            Assert.fail();
    }
}
