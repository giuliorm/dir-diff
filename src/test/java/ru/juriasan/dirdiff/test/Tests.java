package ru.juriasan.dirdiff.test;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class Tests {

    private static final String RESOURCES_PATH  = Paths.get("src", "test", "resources").toString();

    @Test
    public void differentFilesWithDifferentNamesTest() {
        new DifferentFilesWithDifferentNames(RESOURCES_PATH).run();
    }

    @Test
    public void differentFilesWithEqualNamesTest() {
        new DifferentFilesWithEqualNames(RESOURCES_PATH).run();
    }

    @Test
    public void manyDirectoriesTest()  {
        new ManyDirectoriesTest(RESOURCES_PATH).run();
    }
}
