package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

public class RolColSumTest {

    @Test
    public void whenSumSecondColumn15() throws ExecutionException, InterruptedException {
        int[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        assertThat(RolColSum.sum(array)[1].colSum()).isEqualTo(15);
        assertThat(RolColSum.asyncSum(array)[1].colSum()).isEqualTo(15);
    }

    @Test
    public void whenSumSecondRow15() throws ExecutionException, InterruptedException {
        int[][] array = {{10, 2, 0}, {3, 7, 1}, {1, 4, 5}};
        assertThat(RolColSum.sum(array)[1].rowSum()).isEqualTo(11);
        assertThat(RolColSum.asyncSum(array)[1].rowSum()).isEqualTo(11);
    }

    @Test
    public void whenSumThirdRow10() throws ExecutionException, InterruptedException {
        int[][] array = {{10, 2, 0}, {3, 7, 1}, {1, 4, 5}};
        assertThat(RolColSum.sum(array)[2].rowSum()).isEqualTo(10);
        assertThat(RolColSum.asyncSum(array)[2].rowSum()).isEqualTo(10);
    }

    @Test
    public void whenSumThirdColumn6() throws ExecutionException, InterruptedException {
        int[][] array = {{10, 2, 0}, {3, 7, 1}, {1, 4, 5}};
        assertThat(RolColSum.sum(array)[2].colSum()).isEqualTo(6);
        assertThat(RolColSum.asyncSum(array)[2].colSum()).isEqualTo(6);
    }
}