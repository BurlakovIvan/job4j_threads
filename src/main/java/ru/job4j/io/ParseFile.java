package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile implements Parse {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    @Override
    public synchronized String getContent(Predicate<Integer> filter) {
        return content(filter);
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
