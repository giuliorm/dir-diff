package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.testng.Assert;
import ru.juriasan.services.FileService;
import ru.juriasan.util.NewFilenameManager;

public class ThreeDifferentFilesWithEqualNamesTest extends BaseTest {

  private Path firstFile;
  private Path secondFile;
  private Path thirdFile;

  private static final String NAME = "Three Different Files With Equal Names";
  private static final int FILE_COUNT = 3;

  public ThreeDifferentFilesWithEqualNamesTest(String rootPath)  {
      super(NAME, rootPath);
  }

  @Override
  public void generateData() throws IOException {
    firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.toRealPath().toString(),
        "file1").toString());
    secondFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.toRealPath().toString(),
        "file1 (1)").toString());
    thirdFile = FileService.getPlainFileManager().create(Paths.get(secondDirectory.toRealPath().toString(),
        "file1").toString());
    FileService.write(thirdFile, "HW!");
  }

  @Override
  public void checkData() throws IOException {
    if ( resultDirectory == null ) {
      Assert.fail();
    }
    List<Path> result = new ArrayList<>(FileService.getDirectoryManager().getFiles(resultDirectory))
        .stream().sorted((f1, f2) -> Long.compare(f1.toFile().length(), f2.toFile().length()))
        .collect(Collectors.toList());
    if ( result.size() != FILE_COUNT ) {
      Assert.fail();
    }
    Path firstFile = result.get(0);
    Path secondFile = result.get(1);
    Path thirdFile = result.get(2);
    String firstName = firstFile.getFileName().toString();
    String secondName = secondFile.getFileName().toString();
    String thirdName  = thirdFile.getFileName().toString();
    String initialName = getInitialName(firstName, secondName);
    if ( initialName == null ) {
      initialName = getInitialName(firstName, thirdName);
      if ( initialName == null ) {
        initialName = getInitialName(secondName, thirdName);
        if ( initialName == null ) {
          Assert.fail();
        } else {
          checkNames(new NewFilenameManager(initialName), secondName, thirdName);
        }
      } else {
        checkNames(new NewFilenameManager(initialName), firstName, thirdName);
      }
    } else {
      checkNames(new NewFilenameManager(initialName), firstName, secondName);
    }

    if ( !FileService.contentEquals(firstFile, this.firstFile) || !FileService.contentEquals(secondFile,
        this.secondFile) || !FileService.contentEquals(thirdFile, thirdFile) ) {
      Assert.fail();
    }
  }
}
