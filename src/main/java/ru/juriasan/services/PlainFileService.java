package ru.juriasan.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import ru.juriasan.util.NewFilenameManager;

public class PlainFileService extends FileService {

  @Override
  public synchronized Path create(String path) throws IOException {
    Path file = Files.createFile(Paths.get(path));
    setReadableAndAssert(file);
    setWriteableAndAssert(file);
    return file;
  }

  @Override
  public Path get(String path) throws IOException {
    Path file = Paths.get(path);
    assertExists(file);
    assertReadable(file);
    return file;
  }

  @Override
  public boolean diff(Path first, Path second, Path result) throws IOException {
    return first == null || second == null || !contentEquals(first, second);
  }

  @Override
  public boolean diff(String first, String second, String result) throws IOException {
    return diff(get(first), get(second), null);
  }

  @Override
  public boolean diff(Set<Path> first, Set<Path> second, Path result) throws IOException {
    Iterator<Path> firstFiles = first.stream().sorted(Comparator.comparing(Path::getFileName)).iterator();
    Iterator<Path> secondFiles = second.stream().sorted(Comparator.comparing(Path::getFileName)).iterator();
    boolean isThereADiff = false;
    while(firstFiles.hasNext() || secondFiles.hasNext()) {
      Path firstNext = firstFiles.hasNext() ? firstFiles.next() : null;
      Path secondNext = secondFiles.hasNext() ? secondFiles.next() : null;
      if (firstNext != null && secondNext != null && compareNames(firstNext, secondNext)) {
        if (diff(firstNext, secondNext, result)) {
          isThereADiff |= true;
          if (!Files.exists(result)) {
            result = FileService.getDirectoryManager().create(result);
          }
          copy(firstNext.toRealPath().toString(), NewFilenameManager.newPath(firstNext, result));
          copy(secondNext.toRealPath().toString(), NewFilenameManager.newPath(secondNext, result));
        }
      } else {
        isThereADiff |= true;

        if (firstNext != null) {
          copy(firstNext.toRealPath().toString(), NewFilenameManager.newPath(firstNext, result));
        }

        if (secondNext != null) {
          copy(secondNext.toRealPath().toString(), NewFilenameManager.newPath(secondNext, result));
        }
      }
    }
    return isThereADiff;
  }

    @Override
    public synchronized void copy(Path source, Path target) throws IOException {
      copyFile(source, target);
    }

    @Override
    public synchronized void copy(String pathSource, String pathTarget) throws IOException {
      Path source = get(pathSource);
      Path target = Paths.get(pathTarget);
      copy(source, target);
    }
}
