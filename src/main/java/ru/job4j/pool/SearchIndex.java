package ru.job4j.pool;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SearchIndex extends RecursiveTask<Integer> {

    private final List<Object> array;
    private final Object object;
    private final int from;
    private final int to;

    public SearchIndex(List<Object> array, Object object, int from, int to) {
        this.array = array;
        this.object = object;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            int rsl = -1;
            for (int i = from; i <= to; i++) {
                if (Objects.equals(array.get(i), object)) {
                    rsl = i;
                    break;
                }
            }
            return rsl;
        }
        int mid = (from + to) / 2;
        SearchIndex leftArray = new SearchIndex(array, object, from, mid);
        SearchIndex rightArray = new SearchIndex(array, object, mid + 1, to);
        leftArray.fork();
        rightArray.fork();
        int left = leftArray.join();
        int right = rightArray.join();
        return left >= 0 ? left : right >= 0 ? right : -1;
    }

    public static Integer search(List<Object> array, Object object) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new SearchIndex(array, object, 0, array.size() - 1));
    }
}
