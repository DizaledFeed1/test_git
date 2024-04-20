package com.example.git.AI;

import com.example.git.transports.Transport;
import com.example.git.transports.Truck;

import java.util.ArrayList;

public class TruckAI extends BaseAI {

    private boolean flag = true;

    public void pause(){
        flag = false;
    }

    public TruckAI(ArrayList arrayList) {
        super(5);
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

                        double deltaX,deltaY;

                        if ((finalX - startX) * -1 > getSpeed()) {
                            deltaX = startX - getSpeed();
                            transport.getImageView().setLayoutX(deltaX);
                        } else {
                            transport.getImageView().setLayoutX(finalX);
                        }
                        if ((finalY - startY) * -1 > getSpeed()) {
                            deltaY = startY - getSpeed();
                            transport.getImageView().setLayoutY(deltaY);
                        }else {
                            transport.getImageView().setLayoutY(finalY);
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

