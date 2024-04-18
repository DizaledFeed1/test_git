package com.example.git.management;

import com.example.git.transports.Transport;

import java.util.ArrayList;

public abstract class BaseAI extends Thread {
    protected volatile boolean running;
    protected ArrayList<Transport> transportList;
    protected int interval;

    public BaseAI(int interval) {
        this.interval = interval;
    }
    public void start() {
        running = true;
        new Thread(this).start();
    }
    public int getInterval() {
        return interval;
    }
    @Override
    public void run() {
            while (running) {
                    try {
                        moveTransport(transportList);
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
            }
        }
    }
    protected abstract void moveTransport(ArrayList<Transport> transport);
}
