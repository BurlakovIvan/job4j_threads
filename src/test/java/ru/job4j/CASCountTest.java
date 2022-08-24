package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {

    public static class Increment implements Runnable {

        private final CASCount count;

        public Increment(CASCount count) {
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                count.increment();
                System.out.printf("%s : %d\n", Thread.currentThread().getName(), count.get());
            }
        }
    }

    @Test
    void whenCountIs60() throws InterruptedException {
        CASCount count = new CASCount();
        Thread first = new Thread(new Increment(count));
        Thread second = new Thread(new Increment(count));
        Thread third = new Thread(new Increment(count));
        first.start();
        second.start();
        third.start();
        first.join();
        second.join();
        third.join();
        assertThat(count.get()).isEqualTo(60);
    }

    @Test
    void whenCountIs20And60() throws InterruptedException {
        CASCount count = new CASCount();
        Thread first = new Thread(new Increment(count));
        Thread second = new Thread(new Increment(count));
        Thread third = new Thread(new Increment(count));
        first.start();
        first.join();
        assertThat(count.get()).isEqualTo(20);
        second.start();
        third.start();
        second.join();
        third.join();
        assertThat(count.get()).isEqualTo(60);
    }
}