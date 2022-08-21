package ru.job4j.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class SaveFile implements Save {

    private final File file;

    public SaveFile(File file) {
        this.file = file;
    }

    @Override
    public synchronized void saveContent(String content) {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < content.length(); i += 1) {
                out.write(content.charAt(i));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
