package ru.juriasan.dirdiff.test.logic.files.equal.content;

import ru.juriasan.dirdiff.test.Test;
import ru.juriasan.dirdiff.test.logic.files.FileTest;

import java.nio.file.Paths;

public class EqualFilesWithEqualNamesTest  extends FileTest {

    public EqualFilesWithEqualNamesTest() {
        super(Paths.get(Test.RESOURCES_PATH,
                "LogicTesting", "PlainDirectories", "EqualNames", "EqualFiles")
                        .toString(),
                "file1.txt",
                "file1.txt");
    }
}
