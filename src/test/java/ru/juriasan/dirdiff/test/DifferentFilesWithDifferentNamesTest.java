package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import ru.juriasan.services.FileService;

public class DifferentFilesWithDifferentNamesTest extends BaseTest {

  private Path firstFile;
  private Path secondFile;

  private static final String NAME = "Different Files with Different Names";

  public DifferentFilesWithDifferentNamesTest(String rootPath) {
      super(NAME, rootPath);
  }

  @Override
  public void generateData() throws IOException {
    firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectory.toRealPath().toString(),
        "file1").toString());
    secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectory.toRealPath().toString(),
        "file2").toString());
    FileService.write(secondFile, "HW!");
  }

  @Override
  public void checkData() throws IOException {
    checkDifferentFiles(firstFile, secondFile);
  }
}
