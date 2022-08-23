package ru.job4j;

import org.junit.jupiter.api.Test;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    void blocking() throws InterruptedException {
        int limit = 5;
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(limit);
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < limit; i++) {
                        System.out.printf("Producer: %d \n", i);
                        try {
                            simpleBlockingQueue.offer(i);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        Thread consumer = new Thread(
                () -> {
                    for (int i = 0; i < limit; i++) {
                        try {
                            System.out.printf("Consumer: %d \n", simpleBlockingQueue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        producer.start();
        producer.join();
        assertThat(simpleBlockingQueue.getQueue()).isEqualTo(List.of(0, 1, 2, 3, 4));
        consumer.start();
        consumer.join();
        assertThat(simpleBlockingQueue.getQueue()).isEqualTo(List.of());
    }
}