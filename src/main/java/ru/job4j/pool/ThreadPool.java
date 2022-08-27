package ru.job4j.pool;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import ru.job4j.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@ThreadSafe
public class ThreadPool {

    private final List<Thread> threads = new LinkedList<>();
    @GuardedBy("this")
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(10);

    public ThreadPool() {
        int size = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(
                    () -> {
                        while (!Thread.currentThread().isInterrupted()) {
                            try {
                                var runnable = tasks.poll();
                                runnable.run();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public synchronized void work(Runnable job) {
            try {
                tasks.offer(job);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
    }

    public void shutdown() {
        synchronized (this) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
    }

    public synchronized List<Thread> getThreads() {
        return new LinkedList<>(threads);
    }

    public synchronized Queue<Runnable> getTasks() {
        return tasks.getQueue();
    }
}
