package com.example.git.management;

public abstract class BaseAI implements Runnable {
    protected volatile boolean running;
    protected final Object lock = new Object(); // Объект блокировки для синхронизации потоков
    protected int interval; // Интервал между действиями AI

    public BaseAI(int interval) {
        this.interval = interval; // Установите интервал между действиями
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Реализация действий AI
                moveTransport();

                // Пауза между действиями AI
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected abstract void moveTransport();
}
