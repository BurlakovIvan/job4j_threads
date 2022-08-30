package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

class RolColSumTest {

    @Test
    void whenSumSecondColumn15() throws ExecutionException, InterruptedException {
        int[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        assertThat(RolColSum.sum(array)[1].getColSum()).isEqualTo(15);
        assertThat(RolColSum.asyncSum(array)[1].getColSum()).isEqualTo(15);
    }

    @Test
    void whenSumSecondRow15() throws ExecutionException, InterruptedException {
        int[][] array = {{10, 2, 0}, {3, 7, 1}, {1, 4, 5}};
        assertThat(RolColSum.sum(array)[1].getRowSum()).isEqualTo(11);
        assertThat(RolColSum.asyncSum(array)[1].getRowSum()).isEqualTo(11);
    }

}