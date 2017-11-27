package ru.juriasan.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class PlainFileManager extends FileManager {

    private static final String CANNOT_CREATE_FILE = "Cannot create new file with name %s";

    @Override
    public File create(String path) throws IOException {
        File file = new File(path);
        if (!file.createNewFile())
            throw new IOException(String.format(CANNOT_CREATE_FILE, path));
        setReadableAndAssert(file);
        setWriteableAndAssert(file);
        return file;
    }

    @Override
    public File get(String path) throws IOException {
        File file = new File(path);
        assertExists(file);
        assertReadable(file);
        return file;
    }

    @Override
    public boolean diff(File first, File second, File result) throws IOException {
        if (first == null || second == null)
            throw new NullPointerException("Compare arguments cannot be null.");
        return !FileUtils.contentEquals(first, second);
    }

    @Override
    public boolean diff(String first, String second, String result) throws IOException {
        return diff(get(first), get(second), null);
    }

    @Override
    public boolean diff(Set<File> first, Set<File> second, File result) throws IOException {
        Iterator<File> firstFiles = first.stream().sorted(Comparator.comparing(File::getName)).iterator();
        Iterator<File> secondFiles = second.stream().sorted(Comparator.comparing(File::getName)).iterator();
        boolean isThereADiff = false;
        while(firstFiles.hasNext() || secondFiles.hasNext()) {
            File firstNext = firstFiles.hasNext() ? firstFiles.next() : null;
            File secondNext = secondFiles.hasNext() ? secondFiles.next() : null;
            if (firstNext != null && secondNext != null && compareNames(firstNext, secondNext)) {
                if (diff(firstNext, secondNext, result)) {
                    isThereADiff |= true;
                    String name = firstNext.getName().equals("") ? secondNext.getName() : firstNext.getName();
                    NewFilenameManager manager = new NewFilenameManager(name);
                    copy(firstNext.getCanonicalPath(), manager.newPath(result));
                    copy(secondNext.getCanonicalPath(), manager.newPath(result));
                }
            }
            else {
                isThereADiff |= true;
                if (firstNext != null)
                    copy(firstNext.getCanonicalPath(), new NewFilenameManager(firstNext.getName()).newPath(result));
                if (secondNext != null)
                    copy(secondNext.getCanonicalPath(), new NewFilenameManager(secondNext.getName()).newPath(result));
            }
        }
        return isThereADiff;
    }
    /**
     * Reads all data from file into memory.
     * @param file
     * @return
     * @throws IOException
     */
    public String readFile(File file) throws IOException {
        if (file == null)
            return null;
        final StringBuilder data  = new StringBuilder();
        Files.lines(file.toPath()).forEach(line -> data.append(line));
        return data.toString();
    }

    @Override
    public void copy(String pathSource, String pathTarget) throws IOException {
        File source = get(pathSource);
        File target = create(pathTarget);
        FileUtils.copyFile(source, target);
    }

    @Override
    public void copy(File source, File target) throws IOException {
        FileUtils.copyFile(source, target);
    }
}
