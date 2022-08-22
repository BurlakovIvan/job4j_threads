package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    private final int limit;

    public SimpleBlockingQueue(int limit) {
        this.limit = limit;
    }

    public void offer(T value) {
        synchronized (this) {
            while (queue.size() >= limit) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            queue.offer(value);
            notify();
        }
    }

    public T poll() {
        synchronized (this) {
            while (queue.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            var result = this.queue.poll();
            notify();
            return result;
        }
    }

    public synchronized Queue<T> getQueue() {
        return new LinkedList<>(queue);
    }
}
