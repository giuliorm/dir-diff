package ru.juriasan.util;

import ru.juriasan.services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewFilenameManager implements DirectoryStream.Filter {

    private final Pattern namePatternNotStrict;
    private final Pattern namePatternStrict;
    private static final String NAME = "%s (%d)";
    private static final String NUMBER_FORM_NOT_STRICT = "%s[ ]?(\\([1-9]*\\))?";
    private static final String NUMBER_FORM_STRICT = "%s[ ]{1}(\\([1-9]+\\))";
    private String sourceName;

    public NewFilenameManager(String name) {
        this.sourceName = name;
        final String initialPattern = String.format(NUMBER_FORM_NOT_STRICT, name);
        this.namePatternNotStrict = Pattern.compile(initialPattern);
        this.namePatternStrict = Pattern.compile(String.format(NUMBER_FORM_STRICT, name));
    }

    public boolean matchesStrictNumberForm(String name) {
        return namePatternStrict.matcher(name).matches();
    }

    public boolean matchesNonStrictNumberForm(String name) {
        return accept(null, name);
    }

    public int getFileNumber(String name) {
        Matcher m = namePatternStrict.matcher(name);
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
        return number != -1 ? String.format(NAME, name, number + 1) : null;
    }

    @Override
    public boolean accept(Path dir, String name) {
        return namePatternNotStrict.matcher(name).matches();
    }

    public static synchronized String newPath(Path source, Path targetDirectory) throws IOException {
        return new NewFilenameManager(source.getFileName().toString()).newPath(targetDirectory);
    }

    public String newPath(Path result) throws IOException {
        if (sourceName == null || sourceName.equals(""))
            throw new IOException("Filename cannot be null or empty!");

        String newName = Paths.get(result.toRealPath(), sourceName).toString();
        Iterable<Path> filter = FileService.getDirectoryManager().getFiles(result, this);
        if (filter.length > 0) {
            Arrays.sort(filter, Comparator.comparing(File::getName));
            String newShortName = newName(filter[filter.length - 1].getName());
            newName = Paths.get(result.getCanonicalPath(), newShortName).toString();
        }
        return newName;
    }
}