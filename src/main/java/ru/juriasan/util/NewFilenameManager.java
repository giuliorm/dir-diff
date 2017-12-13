package ru.juriasan.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import ru.juriasan.services.FileService;

public class NewFilenameManager implements DirectoryStream.Filter<Path> {

    private Pattern namePatternNotStrict;
    private Pattern namePatternStrict;
    private static final String numberPatternString = "\\(([1-9]+[0-9]*)\\)$";
    private static final Pattern numberPattern = Pattern.compile(numberPatternString);
    private static final Pattern anyNamePatternNonStrict = Pattern.compile("(.*([ ]\\(([1-9]+[0-9]*)\\))?)");
    private static final String NAME = "%s (%d)";
    private static final String NUMBER_FORM_NOT_STRICT = "%s[ ]?(\\([1-9]*\\))?";
    private static final String NUMBER_FORM_STRICT = "%s[ ]?(\\([1-9]+[0-9]*\\))";
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

    public String newName(String name) {
        Matcher m = anyNamePatternNonStrict.matcher(name);
        String newName = String.format("%s (1)", name);
        if (m.find()) {
            try {
                Matcher numberMatcher = numberPattern.matcher(name);
                if (numberMatcher.find()) {
                    String num = numberMatcher.group(1);
                    int number = Integer.parseInt(num);
                    newName = numberMatcher.replaceAll(String.format("(%d)", number + 1));
                }

            }
            catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return newName;
    }

    @Override
    public boolean accept(Path file) throws IOException {
        return anyNamePatternNonStrict.matcher(file.getFileName().toString()).matches();
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