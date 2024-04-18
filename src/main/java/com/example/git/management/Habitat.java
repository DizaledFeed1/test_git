package com.example.git.management;

import com.example.git.transports.Passenger;
import com.example.git.transports.Truck;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;


import java.io.FileNotFoundException;
import java.util.Random;

public class Habitat {
    @FXML
    private Pane imgPane;
    private static final int width = 1260;
    private static final int height = 810;
    private CarContainer carContainer = CarContainer.getInstance();
    private float p1 = 0.5f; //вероятность грузовых авто
    private float p2 = 0.5f; //вероятность пассажирских авто
    private int n1 = 2;
    private int n2 = 3;
    public int lifeTimeN1 = 5;
    public int lifeTimeN2 = 7;
    int finalX,finalY,startX,startY;

    public void setTruckProbability(float probability) {
        this.p1 = probability;
    }
    public void setTruckTime(int time) {
        this.n1 = time;
    }
    public void setLifeTimeN1(int lifeTimeN1){
        this.lifeTimeN1 = lifeTimeN1;
    }
    public void setPassengerProbability(float probability) {
        this.p2 = probability;
    }
    public void setPassengerTime(int time) {
        this.n2 = time;
    }
    public void setLifeTimeN2(int lifeTimeN2){
        this.lifeTimeN2 = lifeTimeN2;
    }
    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
    public int Update(long time) throws FileNotFoundException {
        long newTime = time/1000;
        int number = 0;
        int x,y;
        Random rand = new Random();
        float p = rand.nextFloat();
        try {
            if ((newTime % n1 == 0) && (p1 >= p)) {
                number++;
                startX = rand.nextInt(0, width - 220);
                startY = rand.nextInt(0, 610 - 200);
                if(startX > 430 || startY > 105) {
                    finalX = rand.nextInt(width / 2 - 200);
                    finalY = rand.nextInt((height - 200) / 2 - 200);
                }else{
                    finalX = startX;
                    finalY = startY;
                }
                Truck truck = new Truck(startX,startY ,finalX,finalY, rand.nextInt(Integer.MAX_VALUE), lifeTimeN1);
                carContainer.addCar(truck,newTime);
            }
            if ((newTime % n2 == 0) && (p2 >= p)) {
                number++;
                startX = rand.nextInt(0, width - 220);
                startY =  rand.nextInt(0, 610 - 200);
                if(startX < 630 || startY < 305) {
                    finalX = rand.nextInt(width / 2, width-220);
                    finalY = rand.nextInt(305, 410);
                }else{
                    finalX = startX;
                    finalY = startY;
                }
                Passenger passenger = new Passenger(startX , startY,finalX,finalY, rand.nextInt(Integer.MAX_VALUE), lifeTimeN2);
                carContainer.addCar(passenger,newTime);
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        return number;
    }
    public CarContainer getCarContainer() {
        return carContainer;
    }
}