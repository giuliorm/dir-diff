package ru.juriasan.services;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    protected static final String FILE_SHOULD_BE_A_DIRECTORY = "The file %s should be a directory";
    protected static final String FILE_IS_NOT_READABLE = "File %s is not readable.";
    protected static final String FILE_IS_NOT_EXECUTABLE = "File %s is not executable.";
    protected static final String CANNOT_OBTAIN_LIST_OF_FILES = "Cannot obtain the list of files from file %s.";

    private volatile static DirectoryService directoryManager;
    private volatile static PlainFileService plainFileManager;

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

    public abstract Path get(String path) throws IOException;
    public abstract Path create(String path) throws IOException;
    public abstract boolean diff(Path first, Path second, Path result) throws IOException;
    public abstract  boolean diff(String first, String second, String result) throws IOException;
    public abstract boolean diff(Set<Path> first, Set<Path> second, Path result) throws IOException;
    public abstract void copy(String pathSource, String pathTarget) throws IOException;
    public abstract void copy(Path source, Path target) throws IOException;

    public boolean compareNames(Path first, Path second) throws IOException {
        return Objects.equals(first.getFileName(), second.getFileName());
    }

    protected static void closeStram(Closeable stream) {
        if (stream == null)
            return;
        try {
            stream.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean contentEquals(InputStream first, InputStream second) throws IOException {
        int ch1, ch2;
        boolean equals = true;
        while((ch1 = first.read())!= -1 && (ch2 = second.read()) != -1) {
            if (ch1 != ch2) {
                equals = false;
                break;
            }
        }
        return equals && first.read() == -1 && second.read() == -1;
    }

    public static boolean contentEquals(Path first, Path second) throws  IOException {
        boolean firstExists = Files.exists(first);
        if (firstExists != Files.exists(second))
            return false;
        else if (!firstExists)
            return true;
        if (Files.isDirectory(first) && Files.isDirectory(second))
            throw new IOException("Cannot compare contents of directories");
        else if (Files.isDirectory(first) || Files.isDirectory(second))
            return false;

        if (first.toFile().length() != second.toFile().length())
            return false;
        //if (!Objects.equals(first.getFileName().toString(), second.getFileName().toString()))
        //    return false;
        InputStream firstReader = null;
        InputStream secondReader = null;
        try {
            firstReader = Files.newInputStream(first);
            secondReader = Files.newInputStream(second);
            return contentEquals(firstReader, secondReader);
        } finally {
            closeStram(firstReader);
            closeStram(secondReader);
        }
    }

    public static void write(Path file, String string) throws IOException {
        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(string, 0, string.length());
        }
    }

    public void copyFile(Path source, Path target) throws IOException {
         Files.copy(source, target);
    }

    public static void setExecutableAndAssert(Path file) throws IOException {
        if (!file.toFile().setExecutable(true))
            throw new IOException(String.format(CANNOT_SET_EXECUTABLE, file.toRealPath()));
    }

    public static void setWriteableAndAssert(Path file) throws IOException {
        if (!file.toFile().setWritable(true))
            throw new IOException(String.format(CANNOT_SET_WRITABLE, file.toRealPath()));
    }

    public static void setReadableAndAssert(Path file) throws IOException {
        if (!file.toFile().setReadable(true))
            throw new IOException(String.format(CANNOT_SET_READABLE, file.toRealPath()));
    }

    public static void assertExecutable(Path file) throws IOException {
        if (!Files.isExecutable(file))
            throw new IOException(String.format(FILE_IS_NOT_EXECUTABLE, file.toRealPath()));
    }

    public static void assertDirectory(Path file) throws IOException {
        if (!Files.isDirectory(file))
            throw new IOException(String.format(FILE_SHOULD_BE_A_DIRECTORY, file.toRealPath()));
    }

    public static void assertReadable(Path file) throws IOException {
        if (!Files.isExecutable(file))
            throw new IOException(String.format(FILE_IS_NOT_READABLE, file.toRealPath()));
    }

    public static void assertExists(Path file) throws IOException {
        if (!Files.exists(file))
            throw new FileAlreadyExistsException(String.format(FILE_DOES_NOT_EXISTS, file.toRealPath()));
    }

}
