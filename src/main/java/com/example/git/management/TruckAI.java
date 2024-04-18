package com.example.git.management;

import com.example.git.transports.Transport;
import com.example.git.transports.Truck;

import java.util.ArrayList;

public class TruckAI extends BaseAI {

    private boolean flag = true;

    public void pause(){
        flag = false;
    }

    public TruckAI(ArrayList arrayList) {
        super(100);
        transportList = arrayList;
        super.start();
    }
    @Override
    protected synchronized void moveTransport(ArrayList<Transport> list) {
        synchronized (this){
                if (flag == false){
                    try {
                        System.out.println("Stop");
                        running = false;
                        this.wait();
                        System.out.println("Start");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            synchronized (list) {
                for (Transport transport : list) {
                    if (transport instanceof Truck) {
                        double startX = transport.getImageView().getLayoutX();
                        double startY = transport.getImageView().getLayoutY();
                        double finalX = transport.getFinalX();
                        double finalY = transport.getFinalY();

                        if (startX > 630 || startY > 305 || (startX != finalX && startY != finalY)) {
                            double deltaX = (finalX - startX) / getInterval();
                            double deltaY = (finalY - startY) / getInterval();

                            transport.getImageView().setLayoutX(startX + deltaX);
                            transport.getImageView().setLayoutY(startY + deltaY);
                        }
                    }
                }
            }
        }
    }
    public synchronized void resumeAI() {
        flag = true;
        running = true;
        super.notify();
    }
}

