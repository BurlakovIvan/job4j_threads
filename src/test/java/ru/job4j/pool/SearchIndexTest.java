package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SearchIndexTest {

    @Test
    void searchIntTen() {
        List<Integer> array = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            array.add(i);
        }
        assertThat(SearchIndex.search(array.toArray(), 10)).isEqualTo(10);
    }

    @Test
    void searchStringTen() {
        List<String> array = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            array.add(String.format("%d", i));
        }
        assertThat(SearchIndex.search(array.toArray(), "10")).isEqualTo(10);
    }
}