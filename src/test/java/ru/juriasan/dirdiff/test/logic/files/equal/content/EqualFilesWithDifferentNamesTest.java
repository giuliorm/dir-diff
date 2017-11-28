package ru.juriasan.dirdiff.test.logic.files.equal.content;

import ru.juriasan.dirdiff.test.Test;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class EqualFilesWithDifferentNamesTest extends Test {

    public EqualFilesWithDifferentNamesTest() {
        super(Paths.get(Test.RESOURCES_PATH,
                "LogicTesting", "PlainDirectories", "DifferentNames", "EqualFiles")
                .toString());
    }
    @Override
    public void generate() throws IOException {
        File firstDirectory = FileService.getDirectoryManager().create(firstDirectoryPath);
        File secondDirectory = new File(secondDirectoryPath);

    }

}
