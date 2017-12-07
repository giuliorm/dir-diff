package ru.juriasan.services;

import ru.juriasan.util.NewFilenameManager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DirectoryService extends FileService {

    private static final String CANNOT_CREATE_DIRECTORY = "Cannot create new directory with name %s";

    @Override
    public synchronized Path create(String path) throws IOException {
        Path file = Paths.get(path);
        return create(file);
    }

    public synchronized Path create(Path file) throws IOException {
        Files.createDirectory(file);
        //assertNotExists(file);
        //if (!file.mkdir())
        //    throw new IOException(String.format(CANNOT_CREATE_DIRECTORY, file.getCanonicalPath()));
        setReadableAndAssert(file);
        setExecutableAndAssert(file);
        return file;
    }

    @Override
    public Path get(String path) throws IOException {
        Path file =Paths.get(path);
        assertExists(file);
        assertDirectory(file);
        assertReadable(file);
        assertExecutable(file);
        return file;
    }

    @Override
    public boolean diff(String first, String second, String result) throws IOException {
        return diff(get(first), get(second), create(result));

    }

    @Override
    public boolean diff(Path first, Path second, Path result) throws IOException {
        if (first == null || second == null || result == null)
            throw new NullPointerException("Diff arguments cannot be null");
        Iterable<Path> firstFiles = getFiles(first, null);
        Iterable<Path> secondFiles = getFiles(second, null);

        Set<Path> firstDirectories = new HashSet<>();
        Set<Path> secondDirectories = new HashSet<>();
        Set<Path> firstPlainFiles = new HashSet<>();
        Set<Path> secondPlainFiles = new HashSet<>();

        for(Path file : firstFiles)
            if (Files.isDirectory(file))
                firstDirectories.add(file);
            else firstPlainFiles.add(file);

        for(Path file : secondFiles)
            if (Files.isDirectory(file))
                secondDirectories.add(file);
            else secondPlainFiles.add(file);

        boolean isThereADiff = diff(firstDirectories, secondDirectories, result);
        isThereADiff |= FileService.getPlainFileManager().diff(firstPlainFiles, secondPlainFiles, result);
        return isThereADiff;
    }

    @Override
    public boolean diff(Set<Path> first, Set<Path> second, Path result) throws IOException {
        Iterator<Path> firstDirectories = first.stream().sorted(Comparator.comparing(Path::getFileName)).iterator();
        Iterator<Path> secondDirectories = second.stream().sorted(Comparator.comparing(Path::getFileName)).iterator();
        boolean isThereDiff = false;
        while(firstDirectories.hasNext() || secondDirectories.hasNext()) {
            Path firstNext = firstDirectories.hasNext() ? firstDirectories.next() : null;
            Path secondNext = secondDirectories.hasNext() ? secondDirectories.next() : null;
            if (firstNext != null && secondNext != null && compareNames(firstNext, secondNext)) {
                Path newDirectory = Paths.get(NewFilenameManager.newPath(firstNext, result));
                isThereDiff |= diff(firstNext, secondNext, newDirectory);
            } else {
                isThereDiff |= true;
                if (firstNext != null)
                    copy(firstNext, result);
                if (secondNext != null)
                    copy(secondNext, result);
            }
        }
        return isThereDiff;
    }

    @Override
    public void copy(String pathSource, String pathTarget) throws IOException {
        Path source = get(pathSource);
        Path target = get(pathTarget);
        copy(source, target);
    }

    @Override
    public void copy(Path source, Path targetDirectory) throws IOException {
        Iterable<Path> sourceFiles = getFiles(source, null);
        Path target = create(NewFilenameManager.newPath(source, targetDirectory));
        if (sourceFiles == null)
            throw new IOException(String.format(CANNOT_OBTAIN_LIST_OF_FILES, source.toRealPath()));
        for (Path file : sourceFiles) {
            FileService.getPlainFileManager().copy(file.toRealPath(), NewFilenameManager.newPath(file, target));
        }
    }

    public Iterable<Path> getFiles(String path, DirectoryStream.Filter filter) throws IOException {
        Path file = get(path);
        return getFiles(file, filter);
    }

    public Iterable<Path> getFiles(Path file, DirectoryStream.Filter filter) throws IOException {
        List<Path> files = new ArrayList<>();
        for (Path f : Files.newDirectoryStream(file)) {
            if (filter != null && filter.accept(f))
                files.add(f);
        }
        return files;
        //return filter == null ? file.iterator().listFiles() : file.listFiles(filter);
    }
}
