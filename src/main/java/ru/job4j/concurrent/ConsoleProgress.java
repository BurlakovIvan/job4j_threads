package ru.job4j.concurrent;

import java.util.List;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        int count = 0;
        List<Character> process = List.of('-', '\\', '|', '/');
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.print("\r Loading ... " + process.get(count++ % process.size()));
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(10000);
        progress.interrupt();
        System.out.println("\n Loading is complete");
    }
}
