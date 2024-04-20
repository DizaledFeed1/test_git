package com.example.git.AI;

import com.example.git.transports.Transport;

import java.util.ArrayList;

public abstract class BaseAI extends Thread {
    protected volatile boolean running;
    protected ArrayList<Transport> transportList;
    protected int interval;
    protected int speed;

    public BaseAI(int speed) {
        this.speed = speed;
    }
    public void start() {
        running = true;
        new Thread(this).start();
    }
    public int getSpeed() {
        return speed;
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
