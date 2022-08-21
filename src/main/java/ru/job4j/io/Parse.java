package ru.job4j.io;

import java.io.File;
import java.util.function.Predicate;

public interface Parse {
    String getContent();

    String getContentWithoutUnicode();
}
