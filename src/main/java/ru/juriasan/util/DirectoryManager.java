package ru.juriasan.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class DirectoryManager extends FileManager {

    private static final String CANNOT_CREATE_DIRECTORY = "Cannot create new directory with name %s";

    @Override
    public synchronized File create(String path) throws IOException {
        File file = new File(path);
        return create(file);
    }

    public synchronized File create(File file) throws IOException {
        assertExists(file);
        if (!file.mkdir())
            throw new IOException(String.format(CANNOT_CREATE_DIRECTORY, file.getCanonicalPath()));
        setReadableAndAssert(file);
        setWriteableAndAssert(file);
        setExecutableAndAssert(file);
        return file;
    }

    @Override
    public File get(String path) throws IOException {
        File file = new File(path);
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
    public boolean diff(File first, File second, File result) throws IOException {
        if (first == null || second == null || result == null)
            throw new NullPointerException("Diff arguments cannot be null");
        File[] firstFiles = getFiles(first, null);
        File[] secondFiles = getFiles(second, null);

        Set<File> firstDirectories = new HashSet<>();
        Set<File> secondDirectories = new HashSet<>();
        Set<File> firstPlainFiles = new HashSet<>();
        Set<File> secondPlainFiles = new HashSet<>();

        for(File file : firstFiles)
            if (file.isDirectory())
                firstDirectories.add(file);
            else firstPlainFiles.add(file);

        for(File file : secondFiles)
            if (file.isDirectory())
                secondDirectories.add(file);
            else secondPlainFiles.add(file);

        return diff(firstDirectories, secondDirectories, result) ||
               FileManager.getPlainFileManager().diff(firstPlainFiles, secondPlainFiles, result);
    }

    @Override
    public boolean diff(Set<File> first, Set<File> second, File result) throws IOException {
        Iterator<File> firstDirectories = first.stream().sorted(Comparator.comparing(File::getName)).iterator();
        Iterator<File> secondDirectories = second.stream().sorted(Comparator.comparing(File::getName)).iterator();
        boolean isThereDiff = false;
        while(firstDirectories.hasNext() || secondDirectories.hasNext()) {
            File firstNext = firstDirectories.hasNext() ? firstDirectories.next() : null;
            File secondNext = secondDirectories.hasNext() ? secondDirectories.next() : null;
            if (firstNext != null && secondNext != null) {
                if (compareNames(firstNext, secondNext)) {
                    String newPath = newPathForEqualNames(firstNext, secondNext, result);
                    File newDirectory = null;
                    try {
                        newDirectory = create(newPath);
                    }
                    catch (IOException e) {
                        System.out.println(String.format(CANNOT_CREATE_DIRECTORY, newPath));
                        e.printStackTrace();
                    }
                    if (newDirectory != null)
                        isThereDiff |= diff(firstNext, secondNext, newDirectory);
                }
            }
        }
        return isThereDiff;
    }

    public File[] getFiles(String path, FilenameFilter filter) throws IOException {
        File file = get(path);
        return getFiles(file, filter);
    }

    public File[] getFiles(File file, FilenameFilter filter) throws IOException {
        File[] files = filter == null ? file.listFiles() : file.listFiles(filter);
        if (files == null)
            throw new IOException(String.format(CANNOT_OBTAIN_LIST_OF_FILES, file.getCanonicalPath()));
        return files;
    }
}
