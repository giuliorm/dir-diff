package ru.juriasan.services;

import ru.juriasan.threading.LoadBalancer;
import ru.juriasan.util.NewFilenameManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class DirectoryService extends FileService {

    private static final String CANNOT_CREATE_DIRECTORY = "Cannot create new directory with name %s";
    protected LoadBalancer loadBalancer = LoadBalancer.getInstance(LoadBalancer.MAXIMUM_POOL_SIZE);

    protected DirectoryService() {

    }

    @Override
    public synchronized File create(String path) throws IOException {
        File file = new File(path);
        return create(file);
    }

    public synchronized File create(File file) throws IOException {
        assertNotExists(file);
        if (!file.mkdir())
            throw new IOException(String.format(CANNOT_CREATE_DIRECTORY, file.getCanonicalPath()));
        setReadableAndAssert(file);
        setExecutableAndAssert(file);
        return file;
    }

    @Override
    public File get(String path) throws IOException {
        File file = new File(path);
        assertExists(file);
        assertDirectory(file);
        assertReadable(file);
        assertExecutable(file);
        return file;
    }

    @Override
    public boolean diff(String first, String second, String result) throws IOException {
        return diff(get(first), get(second), create(result));

    }

    @Override
    public boolean diff(File first, File second, File result) throws IOException {
        if (first == null || second == null || result == null)
            throw new NullPointerException("Diff arguments cannot be null");
        File[] firstFiles = getFiles(first, null);
        File[] secondFiles = getFiles(second, null);

        Set<File> firstDirectories = new HashSet<>();
        Set<File> secondDirectories = new HashSet<>();
        Set<File> firstPlainFiles = new HashSet<>();
        Set<File> secondPlainFiles = new HashSet<>();

        for(File file : firstFiles)
            if (file.isDirectory())
                firstDirectories.add(file);
            else firstPlainFiles.add(file);

        for(File file : secondFiles)
            if (file.isDirectory())
                secondDirectories.add(file);
            else secondPlainFiles.add(file);

        boolean isThereADiff = diff(firstDirectories, secondDirectories, result);
        isThereADiff |= FileService.getPlainFileManager().diff(firstPlainFiles, secondPlainFiles, result);
        return isThereADiff;
    }

    @Override
    public boolean diff(Set<File> first, Set<File> second, File result) throws IOException {
        Iterator<File> firstDirectories = first.stream().sorted(Comparator.comparing(File::getName)).iterator();
        Iterator<File> secondDirectories = second.stream().sorted(Comparator.comparing(File::getName)).iterator();
        boolean isThereDiffOuter = false;
        try {
            isThereDiffOuter = loadBalancer.submit(() -> {
                boolean isThereDiff = false;
                while (firstDirectories.hasNext() || secondDirectories.hasNext()) {
                    File firstNext = firstDirectories.hasNext() ? firstDirectories.next() : null;
                    File secondNext = secondDirectories.hasNext() ? secondDirectories.next() : null;
                    if (firstNext != null && secondNext != null && compareNames(firstNext, secondNext)) {
                        File newDirectory = new File(NewFilenameManager.newPath(firstNext, result));
                        isThereDiff |= diff(firstNext, secondNext, newDirectory);
                    } else {
                        isThereDiff |= true;
                        if (firstNext != null)
                            copy(firstNext, result);
                        if (secondNext != null)
                            copy(secondNext, result);
                    }
                }
                return isThereDiff;
            });
        }
        catch (IOException ioException) {
            throw ioException;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return isThereDiffOuter;
    }

    @Override
    public void copy(String pathSource, String pathTarget) throws IOException {
        File source = get(pathSource);
        File target = get(pathTarget);
        copy(source, target);
    }

    @Override
    public void copy(File source, File targetDirectory) throws IOException {
        File[] sourceFiles = getFiles(source, null);
        File target = create(NewFilenameManager.newPath(source, targetDirectory));
        if (sourceFiles == null)
            throw new IOException(String.format(CANNOT_OBTAIN_LIST_OF_FILES, source.getCanonicalPath()));
        for (File file : sourceFiles) {
            FileService.getPlainFileManager().copy(file.getCanonicalPath(), NewFilenameManager.newPath(file, target));
        }
    }

    public File[] getFiles(String path, FilenameFilter filter) throws IOException {
        File file = get(path);
        return getFiles(file, filter);
    }

    public File[] getFiles(File file, FilenameFilter filter) throws IOException {
        return filter == null ? file.listFiles() : file.listFiles(filter);
    }
}
