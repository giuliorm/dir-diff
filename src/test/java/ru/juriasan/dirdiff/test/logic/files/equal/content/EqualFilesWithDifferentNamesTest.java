package ru.juriasan.dirdiff.test.logic.files.equal.content;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import ru.juriasan.dirdiff.test.Test;
import ru.juriasan.dirdiff.test.logic.files.FileTest;
import ru.juriasan.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class EqualFilesWithDifferentNamesTest extends FileTest {

    public EqualFilesWithDifferentNamesTest() {
        super(Paths.get(Test.RESOURCES_PATH,
                "LogicTesting", "PlainDirectories", "DifferentNames", "EqualFiles")
                .toString(),
                "file1.txt",
                "file2.txt");
    }

    //@Override
   // public void generateData() throws IOException {
//        firstDirectory = FileService.getDirectoryManager().create(firstDirectoryPath);
//        secondDirectory = FileService.getDirectoryManager().create(secondDirectoryPath);
//        resultDirectory = FileService.getDirectoryManager().create(resultDirectoryPath);
//        firstFile = FileService.getPlainFileManager().create(Paths.get(firstDirectoryPath,
//                "file1.txt").toString());
//        secondFile = FileService.getPlainFileManager().create(Paths.get(secondDirectoryPath,
//                "file2.txt").toString());
  //  }

    //@Override
   // public void checkResults() throws IOException {

   // }
}
