package ru.job4j.thread;

import ru.job4j.concurrent.ConsoleProgress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {

    private static final int SIZE_BUFFER = 1024;
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    public static void validation(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Переданы не все параметры");
        }
        String url = args[0];
        if (!url.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            throw new IllegalArgumentException("Некорректный url адрес");
        }
        int speed;
        try {
            speed = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Второй параметр не является числом");
        }
        if (speed <= 0) {
            throw new IllegalArgumentException("Скорость не может быть отрицательной или равна нулю");
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Start loading ... ");
            File tempFile = Files.createTempFile(null, null).toFile();
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
                byte[] dataBuffer = new byte[SIZE_BUFFER];
                int bytesRead;
                long start = System.currentTimeMillis();
                while ((bytesRead = in.read(dataBuffer, 0, SIZE_BUFFER)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                    long finish = System.currentTimeMillis();
                    long diff = finish - start;
                    if (SIZE_BUFFER > speed) {
                        /*
                        Сравниваем со скоростью 1024 б/сек, и скачиваем порциями по 1024 байта (SIZE_BUFFER)
                        Если скорость скачивания задана меньше этого значения, то определяем время как
                        1024/speed - узнаем во сколько раз скачали больше чем необходимо, то есть сколько миллисекунд
                        шла бы загрузка при заданной скорости и из нее вычитаем уже затраченное время, если
                        оно меньше чем вычисленное
                         */
                        long time = (SIZE_BUFFER * 1000) / speed;
                        if (time > diff) {
                            Thread.sleep(time - diff);
                        }
                    }
                    start = System.currentTimeMillis();
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validation(args);
        Thread progress = new Thread(new ConsoleProgress());
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        progress.start();
        wget.join();
        progress.interrupt();
        System.out.println("\nLoading is complete");
    }
}
