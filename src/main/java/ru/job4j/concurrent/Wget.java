package ru.job4j.concurrent;

public class Wget {
    public static void main(String[] args) {
        Thread load = new Thread(
                () -> {
                    for (int i = 0; i <= 100; i++) {
                        try {
                            System.out.print("\rLoading : " + i + "%");
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );
        load.start();
    }
}
