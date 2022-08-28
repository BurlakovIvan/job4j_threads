package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ThreadPoolTest {
    @Test
    void threadPoolTest() {
        ThreadPool threadPool = new ThreadPool();
        for (int i = 0; i < 1000; i++) {
            int taskNumber = i;
            try {
                threadPool.work(() -> System.out.printf("Thread: %s. Task number: %d\n",
                        Thread.currentThread().getName(), taskNumber));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        threadPool.shutdown();
        assertThat(threadPool.getThreads().size())
                .isEqualTo(Runtime.getRuntime().availableProcessors());
        assertThat(threadPool.getTasks().size()).isEqualTo(0);
    }

}