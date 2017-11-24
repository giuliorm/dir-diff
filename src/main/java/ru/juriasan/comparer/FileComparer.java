package ru.juriasan.comparer;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class FileComparer {

    protected  static Map<String, File> cash = new HashMap<>();
    protected FileComparer next;

    public FileComparer() { this.next = null; }

    public FileComparer(FileComparer next) {
        this.next = next;
    }



    protected void putToCash(File file) throws IOException {

    }

    public abstract boolean comparePair(File first, File second) throws Exception;



    public boolean compare(File first, File second) throws Exception {
        return next != null ? comparePair(first, second) && next.compare(first, second) : comparePair(first, second);
    }
}
