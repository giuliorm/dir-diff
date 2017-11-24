package ru.juriasan.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class FileManager {

    private static final String CANNOT_CREATE_FILE = "Cannot create new file with name %s";
    private static final String CANNOT_SET_READABLE = "Cannot set file %s as readable. Possibly, user doesn't have" +
            "enough permissions.";
    private static final String CANNOT_SET_WRITABLE = "Cannot set file %s as writeable. Possibly, user doesn't have" +
            "enough permissions.";
    private static final String CANNOT_SET_EXECUTABLE = "Cannot set file %s as executable. Possibly, user doesn't have" +
            "enough permissions.";
    private static final String FILE_ALREADY_EXISTS = "File %s already exists.";
    private static final String CANNOT_OBTAIN_LIST_OF_FILES = "Cannot obtain the list of files from file %s.";
    private static final String FILE_SHOULD_BE_A_DIRECTORY = "The file %s should be a directory";
    private static final String FILE_IS_NOT_READABLE = "File %s is not readable.";
    private static final String FILE_IS_NOT_WRITEABLE = "File %s is not writeable.";
    private static final String FILE_IS_NOT_EXECUTABLE = "File %s is not executable.";
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

    private static void setExecutableAndAssert(File file) throws IOException {
        if (!file.setExecutable(true))
            throw new IOException(String.format(CANNOT_SET_EXECUTABLE, file.getCanonicalPath()));
    }

    private static void setWriteableAndAssert(File file) throws IOException {
        if (!file.setWritable(true))
            throw new IOException(String.format(CANNOT_SET_WRITABLE, file.getCanonicalPath()));
    }

    private static void setReadableAndAssert(File file) throws IOException {
        if (!file.setReadable(true))
            throw new IOException(String.format(CANNOT_SET_READABLE, file.getCanonicalPath()));
    }

    public static void assertExecutable(File file) throws IOException {
        if (!file.canRead())
            throw new IOException(String.format(FILE_IS_NOT_EXECUTABLE, file.getCanonicalPath()));
    }

    public static void assertDirectory(File file) throws IOException {
        if (!file.isDirectory())
            throw new IOException(String.format(FILE_SHOULD_BE_A_DIRECTORY, file.getCanonicalPath()));
    }

    public static void assertReadable(File file) throws IOException {
        if (!file.canRead())
            throw new IOException(String.format(FILE_IS_NOT_READABLE, file.getCanonicalPath()));
    }

    public static void assertExists(File file) throws IOException {
        if (file.exists())
            throw new FileAlreadyExistsException(String.format(FILE_ALREADY_EXISTS, file.getCanonicalPath()));
    }

    public synchronized File createDirectory(File file) throws IOException {
        assertExists(file);
        if (!file.mkdir())
            throw new IOException(String.format(CANNOT_CREATE_FILE, file.getCanonicalPath()));
        setReadableAndAssert(file);
        setWriteableAndAssert(file);
        setExecutableAndAssert(file);
        return file;
    }
    public synchronized File createDirectory(String path) throws IOException {
        File file = new File(path);
        return createDirectory(file);
    }

    public Set<File> getFiles(String path) throws IOException {
        File file = getDirectory(path);
        return getFiles(file);
    }

    public Set<File> getFiles(File file) throws IOException {
        File[] files = file.listFiles();
        if (files == null)
            throw new IOException(String.format(CANNOT_OBTAIN_LIST_OF_FILES, file.getCanonicalPath()));
        return new HashSet<>(Arrays.asList(files));
    }

    public Map<String, File> join(Collection<File> first, Collection<File> second) throws IOException {
        Map<String, File> files = new HashMap<>();
        for (File file : first)
            files.putIfAbsent(file.getCanonicalPath(), file);
        for(File file : second)
            files.putIfAbsent(file.getCanonicalPath(), file);
        return files;
    }

    public boolean compare(File first, File second) throws IOException {
        if (first == null || second == null)
            throw new NullPointerException("Compare arguments cannot be null.");
        return FileUtils.contentEquals(first, second);
    }

    public boolean comparePath(File first, File second) throws IOException {
        if (first == null || second == null)
            throw new NullPointerException("Compare arguments cannot be null.");
        return Objects.equals(first.getCanonicalPath(), second.getCanonicalPath());
    }
    public void copyFile(String pathSource, String pathTarget) throws IOException {
        File source = new File(pathSource);
        File target = new File(pathTarget);
        FileUtils.copyFile(source, target);
    }

    public File getDirectory(String path) throws IOException {
        File file = new File(path);
        assertExists(file);
        assertDirectory(file);
        assertReadable(file);
        assertExecutable(file);
        return file;
    }

    private void handleDirectories(Set<File> directories) {
        
    }

    public void diff(File first, File second, File result) throws IOException {
        if (first == null || second == null || result == null)
            throw new NullPointerException("Diff arguments cannot be null");
        Map<String, File> filesList = join(getFiles(first), getFiles(second));
    }

    public void diff(String first, String second, String result) throws IOException {
        diff(getDirectory(first), getDirectory(second), createDirectory(result));
       // Set<File> directories = filesList.stream().filter(file -> file.isDirectory())
        //        .collect(Collectors.toSet());

    }
}
