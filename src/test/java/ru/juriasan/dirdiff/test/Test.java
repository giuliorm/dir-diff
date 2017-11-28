package ru.juriasan.dirdiff.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Test {

    protected String rootPath;
    protected String firstDirectoryPath;
    protected String secondDirectoryPath;
    protected String resultDirectoryPath;

    public Test(String rootPath) {
        if (rootPath == null)
            throw new RuntimeException("RootPath cannot be null!");
        this.rootPath = rootPath;
        this.firstDirectoryPath = Paths.get(rootPath, "dir1").toString();
        this.secondDirectoryPath = Paths.get(rootPath, "dir2").toString();
        this.resultDirectoryPath = Paths.get(rootPath, "dir3").toString();
    }

    protected static final String RESOURCES_PATH = Paths
            .get("src", "main", "test", "resources").toString();

    public abstract void generate() throws IOException;

    private void removeAllFiles(String directoryPath, File directory) throws IOException {
        if (!removeAllFilesRecursive(directory))
            System.out.println(String.format("Cannot remove directory %s", directoryPath));
    }

    private boolean removeAllFilesRecursive(File directory) throws IOException {
        boolean result = true;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files)
                if (file.isDirectory())
                    result &= removeAllFilesRecursive(file);
                else
                    result &= file.delete();
        }
        else {
            System.out.println(String.format("Cannot list contents of %s",
                    directory.getCanonicalPath()));
        }

        result &= directory.delete();
        return result;
    }

    public void clean() throws IOException {
        removeAllFiles(firstDirectoryPath, new File(firstDirectoryPath));
        removeAllFiles(secondDirectoryPath, new File(secondDirectoryPath));
        removeAllFiles(resultDirectoryPath, new File(resultDirectoryPath));
    }
}
