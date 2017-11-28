package ru.juriasan;

import ru.juriasan.services.FileService;
import ru.juriasan.util.CLI;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //String dir1 = "C:\\Users\\zotovy\\Documents\\Git\\dirdiff\\src\\main\\resources\\dir1";
        //String dir2 = "C:\\Users\\zotovy\\Documents\\Git\\dirdiff\\src\\main\\resources\\dir2";
        //String dir3 = "C:\\Users\\zotovy\\Documents\\Git\\dirdiff\\src\\main\\resources\\dir3";
        CLI cli = new CLI(args);
        FileService.getDirectoryManager().diff(cli.getFirstDirectory(), cli.getSecondDirectory(),
                cli.getResultDirectory());
    }
}
