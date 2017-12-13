package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import ru.juriasan.services.FileService;
import ru.juriasan.util.NewFilenameManager;

public abstract  class BaseTest {

  protected String rootDirectoryPath;
  protected String name;

  protected Path firstDirectory;
  protected Path secondDirectory;
  protected Path resultDirectory;

  public BaseTest(String name, String rootDirectoryPath)  {
    this.name = name;
    this.rootDirectoryPath = rootDirectoryPath;
  }

  private void createRoots() throws IOException {
    Path root = Paths.get(rootDirectoryPath);
    if ( !Files.exists(root) ) {
      FileService.getDirectoryManager().create(root);
    }
    this.firstDirectory = FileService.getDirectoryManager().create(Paths.get(rootDirectoryPath, "first"));
    this.secondDirectory = FileService.getDirectoryManager().create(Paths.get(rootDirectoryPath, "second"));
    this.resultDirectory = FileService.getDirectoryManager().create(Paths.get(rootDirectoryPath, "result"));
  }

  public void clean(Path dir) throws IOException {
    if ( !Files.exists(dir) ) {
      return;
    }
    if ( !Files.isDirectory(dir) ) {
      Files.delete(dir);
      return;
    }
    List<Path> files = new ArrayList<>(FileService.getDirectoryManager().getFiles(dir));
    for ( Path file : files ) {
      if ( Files.isDirectory(file) ) {
          clean(file);
      } else Files.delete(file);
    }
    Files.delete(dir);
  }

  public void clean(String rootDirectory) throws IOException {
    Path dir = Paths.get(rootDirectory);
    clean(dir);
  }

  public abstract void generateData() throws IOException;
  public abstract void checkData() throws IOException;

  public void run() throws IOException {
    Throwable ex = null;
    try {
      clean(rootDirectoryPath);
      createRoots();
      generateData();
      double startTime = System.currentTimeMillis();
      FileService.getDirectoryManager().diff(firstDirectory, secondDirectory, resultDirectory);
      double delay = System.currentTimeMillis() - startTime;
      System.out.println(String.format("Time elapsed %f", delay));
      checkData();
    }
    catch ( Throwable e ) {
      e.printStackTrace();
      ex = e;
    }
    finally {
      clean(rootDirectoryPath);
      if ( ex != null ) {
        Assert.fail();
      }
    }
  }

  public void checkDifferentFiles(Path first, Path second) throws IOException {
    final int fileCount = 2;
    if ( resultDirectory == null ) {
      Assert.fail();
    }
    List<Path> result = new ArrayList<>(FileService.getDirectoryManager().getFiles(resultDirectory));
    if ( result.size() != fileCount ) {
      Assert.fail();
    }
    Path firstFile = result.get(0);
    Path secondFile = result.get(1);
    if ( !FileService.contentEquals(firstFile, first) || !FileService.contentEquals(secondFile, second) ) {
      Assert.fail();
    }
  }

  protected String getInitialName(String firstName, String secondName) {
    if ( firstName.contains(secondName) ) {
      return secondName;
    }
    if ( secondName.contains(firstName) ) {
      return firstName;
    }
    return null;
  }

  protected void checkNames(NewFilenameManager manager, String firstName, String secondName) {
    if ( manager.matchesStrictNumberForm(firstName)) {
      if ( manager.matchesStrictNumberForm(secondName) ||
          !manager.matchesNonStrictNumberForm(secondName) ) {
        Assert.fail();
      }
    } else if ( !manager.matchesNonStrictNumberForm(firstName) ||
        !manager.matchesStrictNumberForm(secondName) ) {
      Assert.fail();
    }
  }
}
