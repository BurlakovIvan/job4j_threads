package ru.job4j.threadlocal;

public class SecondThread extends Thread {
    @Override
    public void run() {
        ThreadLocalDemo.tl.set("Это поток 2."); // записываем данные в переменную ThreadLocal второй нити
        System.out.println(ThreadLocalDemo.tl.get()); // получаем данные из этой переменной и выводим на печать
    }
}
