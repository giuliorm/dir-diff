package ru.juriasan.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EqualFilenameFilter implements FilenameFilter {

    private final String name;

    public EqualFilenameFilter(String name) {
        this.name = name;
    }

    @Override
    public boolean accept(File dir, String name) {
        return Objects.equals(name, this.name);
    }
}

public class NewFilenameManager implements FilenameFilter {

    private final Pattern namePattern;
    private static final String NAME = "%s (%d)";
    private static final String REGEX = "%s (\\([1-9]+\\))";

    public NewFilenameManager(String name) {
        final String initialPattern = String.format(REGEX, name);
        this.namePattern = Pattern.compile(initialPattern);
    }

    private int getFileNumber(String name) {
        Matcher m = namePattern.matcher(name);
        int number = 0;
        if (m.matches()) {
            try {
                number = Integer.parseInt(m.group());
            }
            catch (NumberFormatException ex) {
                number = -1;
            }
        }
        return number;
    }

    public String newName(String name) {
        int number = getFileNumber(name);
        if (number == 0)
            return name;
        return number != -1 ? String.format(NAME, name, number + 1) : null;
    }

    @Override
    public boolean accept(File dir, String name) {
        return namePattern.matcher(name).matches();
    }

    public synchronized String newPath(String name, File result) throws IOException {
        if (name == null || name.equals(""))
            throw new IOException("Filename cannot be null or empty!");

        String newName = Paths.get(result.getCanonicalPath(), name).toString();
        File[] filterByName =  FileManager.getDirectoryManager().getFiles(result, new EqualFilenameFilter(name));
        if (filterByName.length > 0) {
            //NewFilenameManager filenameFilter = new NewFilenameManager(name);
            File[] filter = FileManager.getDirectoryManager().getFiles(result, this);
            if (filter.length > 0) {
                Arrays.sort(filter, Comparator.comparing(File::getName));
                String newShortName = newName(filter[filter.length - 1].getName());
                newName = Paths.get(result.getCanonicalPath(), newShortName).toString();
            }
        }
        return newName;
    }
}