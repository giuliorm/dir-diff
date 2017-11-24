package ru.juriasan.util;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.*;

public class FileManager {

    private static final String CANNOT_CREATE_FILE = "Cannot create new file with name %s";
    private static final String CANNOT_SET_READABLE = "Cannot set file %s as readable. Possibly, user doesn't have" +
            "enough permissions.";
    private static final String CANNOT_SET_WRITABLE = "Cannot set file %s as writeable. Possibly, user doesn't have" +
            "enough permissions.";
    private static final String CANNOT_SET_EXECUTABLE = "Cannot set file %s as executable. Possibly, user doesn't have" +
            "enough permissions.";
    private static final String FILE_ALREADY_EXISTS = "File %s already exists.";
    private static final String CANNOT_OBTAIN_LIST_OF_FILES = "Cannot obtain the list of files from file %s. The file" +
            "should be a directory.";
    private static final String FILE_IS_NOT_READABLE = "File %s is not readable.";
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

    public static void assertReadable(File file) throws IOException {
        if (!file.canRead())
            throw new FileAlreadyExistsException(String.format(FILE_IS_NOT_READABLE, file.getCanonicalPath()));
    }

    public static void assertExists(File file) throws IOException {
        if (file.exists())
            throw new FileAlreadyExistsException(String.format(FILE_ALREADY_EXISTS, file.getCanonicalPath()));
    }

    public synchronized File createDirectory(String path) throws IOException {
        File file = new File(path);
        assertExists(file);
        if (!file.mkdir())
            throw new IOException(String.format(CANNOT_CREATE_FILE, path));
        setReadableAndAssert(file);
        setWriteableAndAssert(file);
        setExecutableAndAssert(file);
        return file;
    }

    public Set<File> getFiles(String path) throws IOException {
        File file = new File(path);
        assertExists(file);
        File[] files = file.listFiles();
        if (files == null)
            throw new IOException(String.format(CANNOT_OBTAIN_LIST_OF_FILES, path));
        return new HashSet<>(Arrays.asList(files));
    }

    public Collection<File> join(Collection<File> first, Collection<File> second) throws IOException {
        Map<String, File> files = new HashMap<>();
        for (File file : first)
            files.putIfAbsent(file.getCanonicalPath(), file);
        for(File file : second)
            files.putIfAbsent(file.getCanonicalPath(), file);
        return files.values();
    }
}
