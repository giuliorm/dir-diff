package ru.juriasan.services;

import ru.juriasan.threading.LoadBalancer;

import ru.juriasan.threading.LoadBalancer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Objects;
import java.util.Set;


public abstract  class FileService {

    protected static final String CANNOT_SET_READABLE = "Cannot set file %s as readable. Possibly, user doesn't have" +
            "enough permissions.";
    protected static final String CANNOT_SET_WRITABLE = "Cannot set file %s as writeable. Possibly, user doesn't have " +
            "enough permissions.";
    protected static final String CANNOT_SET_EXECUTABLE = "Cannot set file %s as executable. Possibly, user doesn't have " +
            "enough permissions.";
    protected static final String FILE_DOES_NOT_EXISTS = "File %s does not exists.";
    protected static final String FILE_ALREADY_EXISTS = "File %s already exists.";
    protected static final String FILE_SHOULD_BE_A_DIRECTORY = "The file %s should be a directory";
    protected static final String FILE_IS_NOT_READABLE = "File %s is not readable.";
    protected static final String FILE_IS_NOT_WRITEABLE = "File %s is not writeable.";
    protected static final String FILE_IS_NOT_EXECUTABLE = "File %s is not executable.";
    protected static final String CANNOT_OBTAIN_LIST_OF_FILES = "Cannot obtain the list of files from file %s.";

    private volatile static DirectoryService directoryManager;
    private volatile static PlainFileService plainFileManager;
    protected LoadBalancer loadBalancer = LoadBalancer.getInstance(LoadBalancer.MAXIMUM_POOL_SIZE);

    public static DirectoryService getDirectoryManager() {
        if (directoryManager == null) {
            synchronized (FileService.class) {
                if (directoryManager == null)
                    directoryManager = new DirectoryService();
            }
        }
        return directoryManager;
    }

    public static FileService getPlainFileManager() {
        if (plainFileManager == null) {
            synchronized (FileService.class) {
                if (plainFileManager == null)
                    plainFileManager = new PlainFileService();
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

    public static void assertNotExists(File file) throws IOException {
        if (file.exists())
            throw new FileAlreadyExistsException(String.format(FILE_ALREADY_EXISTS, file.getCanonicalPath()));
    }

    public static void assertExists(File file) throws IOException {
        if (!file.exists())
            throw new FileAlreadyExistsException(String.format(FILE_DOES_NOT_EXISTS, file.getCanonicalPath()));
    }
}
