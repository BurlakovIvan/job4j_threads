package ru.job4j.thread;

import ru.job4j.concurrent.ConsoleProgress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {

    private static final int SIZE_BUFFER = 1024;
    private static final int TIME_DOWNLOAD = 1000;
    private final String url;
    private final String file;
    private final int speed;

    public Wget(String url, String file, int speed) {
        this.url = url;
        this.file = file;
        this.speed = speed;
    }

    public static void validation(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Переданы не все параметры");
        }
        String url = args[0];
        if (!url.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            throw new IllegalArgumentException("Некорректный url адрес");
        }
        File file = new File(args[1]);
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("Не существует %s", file.getAbsoluteFile()));
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException(String.format("Не является файлом %s", file.getAbsoluteFile()));
        }
        int speed;
        try {
            speed = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Второй параметр не является числом");
        }
        if (speed <= 0) {
            throw new IllegalArgumentException("Скорость не может быть отрицательной или равна нулю");
        }
    }

    @Override
    public void run() {
        System.out.println("Start loading ... ");
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[SIZE_BUFFER];
            int bytesRead;
            int downloadDate = 0;
            long start = System.currentTimeMillis();
            while ((bytesRead = in.read(dataBuffer, 0, SIZE_BUFFER)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                downloadDate += bytesRead;
                if (downloadDate >= speed) {
                    long finish = System.currentTimeMillis();
                    long diff = finish - start;
                    if (diff < TIME_DOWNLOAD) {
                        Thread.sleep(TIME_DOWNLOAD - diff);
                    }
                    downloadDate = 0;
                    start = System.currentTimeMillis();
                }
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validation(args);
        long start = System.currentTimeMillis();
        Thread progress = new Thread(new ConsoleProgress());
        String url = args[0];
        String file = args[1];
        int speed = Integer.parseInt(args[2]);
        Thread wget = new Thread(new Wget(url, file, speed));
        wget.start();
        progress.start();
        wget.join();
        progress.interrupt();
        long finish = System.currentTimeMillis();
        System.out.printf("\nLoading is complete. Time download : %d", (finish - start) / TIME_DOWNLOAD);
    }
}
