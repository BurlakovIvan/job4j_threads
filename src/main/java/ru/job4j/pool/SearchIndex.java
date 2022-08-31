package ru.job4j.pool;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SearchIndex<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final T object;
    private final int from;
    private final int to;

    public SearchIndex(T[] array, T object, int from, int to) {
        this.array = array;
        this.object = object;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return computeLine();
        }
        int mid = (from + to) / 2;
        SearchIndex<T> leftArray = new SearchIndex<>(array, object, from, mid);
        SearchIndex<T> rightArray = new SearchIndex<>(array, object, mid + 1, to);
        leftArray.fork();
        rightArray.fork();
        int left = leftArray.join();
        int right = rightArray.join();
        return Math.max(left, right);
    }

    private int computeLine() {
        int rsl = -1;
        for (int i = from; i <= to; i++) {
            if (Objects.equals(array[i], object)) {
                rsl = i;
                break;
            }
        }
        return rsl;
    }

    public static <T> Integer search(T[] array, T object) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new SearchIndex<>(array, object, 0, array.length - 1));
    }
}
