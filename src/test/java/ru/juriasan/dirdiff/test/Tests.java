package ru.juriasan.dirdiff.test;

import org.testng.annotations.Test;

import java.nio.file.Paths;

public class Tests {

    private static final String RESOURCES_PATH  = Paths.get("src", "test", "resources").toString();

    @Test
    public void differentFilesWithDifferentNamesTest() {
        new DifferentFilesWithDifferentNamesTest(RESOURCES_PATH).run();
    }

    @Test
    public void differentFilesWithEqualNamesTest() {
        new DifferentFilesWithEqualNames(RESOURCES_PATH).run();
    }

    @Test
    public void equalFilesWithEqualNamesTest() {
        new EqualFilesWithEqualNamesTest(RESOURCES_PATH).run();
    }

    @Test
    public void equalFilesWithDifferentNamesTest() {
        new EqualFilesWithDifferentNamesTest(RESOURCES_PATH).run();
    }

    @Test(enabled = false)
    public void manyDirectoriesTest()  {
        new ManyDirectoriesTest(500, RESOURCES_PATH).run();
    }
}
