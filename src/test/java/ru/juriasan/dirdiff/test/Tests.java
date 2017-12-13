package ru.juriasan.dirdiff.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.annotations.Test;
import ru.juriasan.services.FileService;

public class Tests {

    private static final String RESOURCES_PATH  = Paths.get("src", "test", "resources").toString();

    private Tests() throws IOException {
        Path resources = Paths.get(RESOURCES_PATH);
        FileService.setReadableAndAssert(resources);
        FileService.setExecutableAndAssert(resources);
    }
    @Test
    public void differentFilesWithDifferentNamesTest() throws IOException {
        String rootPath = Paths.get(RESOURCES_PATH, "differentFilesDifferentNames").toString();
        new DifferentFilesWithDifferentNamesTest(rootPath).run();
    }

    @Test
    public void differentFilesWithEqualNamesTest() throws IOException {
        String rootPath = Paths.get(RESOURCES_PATH, "differentFilesEqualNames").toString();
        new DifferentFilesWithEqualNames(rootPath).run();
    }

    @Test
    public void equalFilesWithEqualNamesTest() throws IOException {
        String rootPath = Paths.get(RESOURCES_PATH, "equalFilesWithEqualNames").toString();
        new EqualFilesWithEqualNamesTest(rootPath).run();
    }

    @Test
    public void equalFilesWithDifferentNamesTest() throws IOException {
        String rootPath = Paths.get(RESOURCES_PATH, "equalFilesWithDifferentNames").toString();
        new EqualFilesWithDifferentNamesTest(rootPath).run();
    }

    @Test
    public void treeDifferentFilesWithEqualNamesTest() throws IOException {
        String rootPath = Paths.get(RESOURCES_PATH, "threeDifferentFilesWithEqualNames").toString();
        new ThreeDifferentFilesWithEqualNamesTest(rootPath).run();
    }

    @Test(enabled = false)
    public void manyDirectoriesTest() throws IOException {
        String rootPath = Paths.get(RESOURCES_PATH, "manyDirs").toString();
        new ManyDirectoriesTest(500, rootPath).run();
    }
}
