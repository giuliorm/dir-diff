package ru.juriasan.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public abstract  class FileManager {

    protected static final String CANNOT_SET_READABLE = "Cannot set file %s as readable. Possibly, user doesn't have" +
            "enough permissions.";
    protected static final String CANNOT_SET_WRITABLE = "Cannot set file %s as writeable. Possibly, user doesn't have" +
            "enough permissions.";
    protected static final String CANNOT_SET_EXECUTABLE = "Cannot set file %s as executable. Possibly, user doesn't have" +
            "enough permissions.";
    protected static final String FILE_ALREADY_EXISTS = "File %s already exists.";
    protected static final String FILE_SHOULD_BE_A_DIRECTORY = "The file %s should be a directory";
    protected static final String FILE_IS_NOT_READABLE = "File %s is not readable.";
    protected static final String FILE_IS_NOT_WRITEABLE = "File %s is not writeable.";
    protected static final String FILE_IS_NOT_EXECUTABLE = "File %s is not executable.";
    protected static final String CANNOT_OBTAIN_LIST_OF_FILES = "Cannot obtain the list of files from file %s.";

    private volatile static DirectoryManager directoryManager;
    private volatile static PlainFileManager plainFileManager;

    public static DirectoryManager getDirectoryManager() {
        if (directoryManager == null) {
            synchronized (FileManager.class) {
                if (directoryManager == null)
                    directoryManager = new DirectoryManager();
            }
        }
        return directoryManager;
    }

    public static FileManager getPlainFileManager() {
        if (plainFileManager == null) {
            synchronized (FileManager.class) {
                if (plainFileManager == null)
                    plainFileManager = new PlainFileManager();
            }
        }
        return plainFileManager;
    }

    public abstract File get(String path) throws IOException;
    public abstract File create(String path) throws IOException;
    public abstract boolean diff(File first, File second, File result) throws IOException;
    public abstract  boolean diff(String first, String second, String result) throws IOException;
    public abstract boolean diff(Set<File> first, Set<File> second, File result) throws IOException;
    public abstract void copy(String pathSource, String pathTarget) throws IOException;
    public abstract void copy(File source, File target) throws IOException;

    public boolean compareNames(File first, File second) throws IOException {
        return Objects.equals(first.getName(), second.getName());
    }

    public boolean comparePath(File first, File second) throws IOException {
        return Objects.equals(first.getCanonicalPath(), second.getCanonicalPath());
    }

    public static void setExecutableAndAssert(File file) throws IOException {
        if (!file.setExecutable(true))
            throw new IOException(String.format(CANNOT_SET_EXECUTABLE, file.getCanonicalPath()));
    }

    public static void setWriteableAndAssert(File file) throws IOException {
        if (!file.setWritable(true))
            throw new IOException(String.format(CANNOT_SET_WRITABLE, file.getCanonicalPath()));
    }

    public static void setReadableAndAssert(File file) throws IOException {
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


}
