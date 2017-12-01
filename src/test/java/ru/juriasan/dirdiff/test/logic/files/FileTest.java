package ru.juriasan.dirdiff.test.logic.files;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import ru.juriasan.dirdiff.test.Test;
import ru.juriasan.services.FileService;

import java.io.*;
import java.nio.file.Paths;

public abstract class FileTest  extends Test {

    protected File firstFile;
    protected File secondFile;
    protected String firstFileName;
    protected String secondFileName;

    public FileTest(String rootPath, String firstFileName, String secondFileName) {
        super(rootPath);
        this.firstFileName = firstFileName;
        this.secondFileName = secondFileName;
    }

    @Override
    public void generateData() throws IOException {
        super.generateData();
        firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectoryPath,
                firstFileName).toString());
        secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectoryPath,
                secondFileName).toString());
    }

    @Override
    public void checkResults() throws IOException {
        if (!resultDirectory.exists())
            Assert.fail();
        File[] files = resultDirectory.listFiles();
        if (files == null || files.length > 2)
            Assert.fail();
        File firstResult = files[0];
        File secondResult = files[1];
        if (!FileUtils.contentEquals(firstFile, firstResult) || !FileUtils.contentEquals(secondFile, secondResult))
            Assert.fail();
    }

    protected void writeToFile(File file, String text) throws IOException {
        try(FileWriter w = new FileWriter(file)) {
            w.write(text);
        }
    }
}
