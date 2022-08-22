package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile implements Parse {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    @Override
    public synchronized String getContent() {
        return content(data -> true);
    }

    @Override
    public synchronized String getContentWithoutUnicode() {
        return content(data -> data < 0x80);
    }

    private String content(Predicate<Integer> filter) {
        StringBuilder output = new StringBuilder();
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            int data;
            while ((data = in.read()) != -1) {
                if (filter.test(data)) {
                    output.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
