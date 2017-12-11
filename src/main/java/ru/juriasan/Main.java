package ru.juriasan;

import ru.juriasan.services.FileService;
import ru.juriasan.util.CLI;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CLI cli = new CLI(args);
        FileService.getDirectoryManager().diff(cli.getFirstDirectory(), cli.getSecondDirectory(),
                cli.getResultDirectory());
    }
}
