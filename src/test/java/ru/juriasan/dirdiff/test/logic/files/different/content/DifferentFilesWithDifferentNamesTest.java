package ru.juriasan.dirdiff.test.logic.files.different.content;

import ru.juriasan.dirdiff.test.Test;
import ru.juriasan.dirdiff.test.logic.files.FileTest;

import java.io.IOException;
import java.nio.file.Paths;

public class DifferentFilesWithDifferentNamesTest  extends FileTest {

    public DifferentFilesWithDifferentNamesTest() {

        super(Paths.get(Test.RESOURCES_PATH,
                "LogicTesting", "PlainDirectories", "DifferentNames", "DifferentFiles")
                        .toString(),
                "file1.txt",
                "file2.txt");
    }

    @Override
    public void generateData() throws IOException {
        super.generateData();
        if (firstFile != null)
            writeToFile(firstFile, "The first file is different from second file.");
    }
}
