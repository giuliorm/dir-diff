package ru.juriasan.dirdiff.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public abstract  class BaseTest {

    String rootDirectoryPath;
    File firstDirectory;
    File secondDirectory;
    File resultDirectory;

    public BaseTest(String rootDirectoryPath)  {
        this.rootDirectoryPath = rootDirectoryPath;
    }

    private void createRoots() throws IOException {
        this.firstDirectory = FileService.getDirectoryManager().create(Paths.get(rootDirectoryPath, "first")
                .toString());
        this.secondDirectory = FileService.getDirectoryManager().create(Paths.get(rootDirectoryPath, "second")
                .toString());
        this.resultDirectory = FileService.getDirectoryManager().create(Paths.get(rootDirectoryPath, "result")
                .toString());
    }

    public boolean clean(File dir) {
        if (!dir.exists())
            return true;
        if (!dir.isDirectory())
            return dir.delete();
        File[] files = dir.listFiles();
        boolean result = true;
        if (files == null)
            return dir.delete();
        for (File file : files) {
            if (file.isDirectory()) {
                result &= clean(file);
                result &= file.delete();
            }else result &= file.delete();
        }

        return result;
    }

    public boolean clean(String rootDirectory) {
        File dir = new File(rootDirectory);
        return clean(dir);
    }

    public abstract void generateData() throws IOException;

    public abstract void checkData() throws IOException ; /*{
        if (resultDirectory == null || !resultDirectory.isDirectory())
            Assert.fail();
        File[] firstFiles = firstDirectory.listFiles();
        File[] secondFiles = secondDirectory.listFiles();
        if (firstFiles == null || secondFiles == null)
            throw new RuntimeException();
        Iterator<File> first =
                Arrays.stream(firstFiles).sorted(Comparator.comparing(File::getName)).iterator();
        Iterator<File> second =  Arrays.stream(secondFiles)
                .sorted(Comparator.comparing(File::getName)).iterator();
        File[] resultFiles = resultDirectory.listFiles();
        if (resultFiles == null)
            throw new RuntimeException();
        Iterator<File> result =
                Arrays.stream(resultFiles).sorted(Comparator.comparing(File::getName)).iterator();

    }
 */
    public void run() {
        try {
            clean(rootDirectoryPath);
            createRoots();
            generateData();
            FileService.getDirectoryManager().diff(firstDirectory, secondDirectory, resultDirectory);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            clean(rootDirectoryPath);
        }
    }
}
