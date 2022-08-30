package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static class Sums {
        private final int rowSum;
        private final int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }
    }

    public static Sums[] sum(int[][] matrix) {
        int n = matrix.length;
        Sums[] result = new Sums[n];
        for (int i = 0; i < n; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            result[i] = new Sums(rowSum, colSum);
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException  {
        int n = matrix.length;
        Sums[] result = new Sums[n];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int k = 0; k < n; k++) {
            futures.put(k, getTask(matrix, k));
        }
        for (Integer key : futures.keySet()) {
            result[key] = futures.get(key).get();
        }
        return result;
    }

    private static CompletableFuture<Sums> getTask(int[][] matrix, int task) {
        return CompletableFuture.supplyAsync(() -> {
            int rowSum = 0;
            int colSum = 0;
            for (int i = 0; i < matrix.length; i++) {
                colSum += matrix[i][task];
                rowSum += matrix[task][i];
            }
            return new Sums(rowSum, colSum);
        });
    }
}
