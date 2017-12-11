package ru.juriasan.util;

import ru.juriasan.services.FileService;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NewFilenameManager implements DirectoryStream.Filter<Path> {

    private final Pattern namePatternNotStrict;
    private final Pattern namePatternStrict;
    private static final String NAME = "%s (%d)";
    private static final String NUMBER_FORM_NOT_STRICT = "%s[ ]?(\\([1-9]*\\))?";
    private static final String NUMBER_FORM_STRICT = "%s[ ]{1}(\\([1-9]+[0]*\\))";
    private String sourceName;

    public NewFilenameManager(String sourceName) {
        this.namePatternNotStrict = Pattern.compile(String.format(NUMBER_FORM_NOT_STRICT, sourceName));
        this.namePatternStrict = Pattern.compile(String.format(NUMBER_FORM_STRICT, sourceName));
        this.sourceName = sourceName;
    }

    public boolean matchesStrictNumberForm(String name) {
        return namePatternStrict.matcher(name).matches();
    }

    public boolean matchesNonStrictNumberForm(String name) {
        return namePatternNotStrict.matcher(name).matches();
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
    public boolean accept(Path file) throws IOException {
        return namePatternNotStrict.matcher(file.getFileName().toString()).matches();
    }

    public static synchronized String newPath(Path source, Path targetDirectory) throws IOException {
        return new NewFilenameManager(source.getFileName().toString()).newPath(targetDirectory);
    }

    public String newPath(Path result) throws IOException {
        if (sourceName == null || sourceName.equals(""))
            throw new IOException("Filename cannot be null or empty!");

        String newName = Paths.get(result.toRealPath().toString(), sourceName).toString();
        List<Path> filter = FileService.getDirectoryManager().getFilesFiltered(result, this)
                .stream().sorted(Comparator.comparing(Path::getFileName)).collect(Collectors.toList());
        if (filter.size() > 0) {
            String newShortName = newName(filter.get(filter.size() - 1).getFileName().toString());
            newName = Paths.get(result.toRealPath().toString(), newShortName).toString();
        }
        return newName;
    }
}