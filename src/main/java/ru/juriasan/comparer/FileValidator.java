package ru.juriasan.comparer;

import ru.juriasan.util.FileManager;

import java.io.File;

public class FileValidator extends FileComparer {

    @Override
    public boolean comparePair(File first, File second) throws Exception {
        if (first == null || second == null)
            throw new IllegalArgumentException("FileComparer arguments cannot be null.");
        FileManager.assertExists(first);
        FileManager.assertExists(second);
        FileManager.assertReadable(first);
        FileManager.assertReadable(second);
        return true;
    }
}
