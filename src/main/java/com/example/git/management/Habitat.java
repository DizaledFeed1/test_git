package com.example.git.management;

import com.example.git.transports.Passenger;
import com.example.git.transports.Truck;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.util.Random;

public class Habitat {
    private static final int width = 1150;
    private static final int height = 720;
    private CarContainer carContainer = CarContainer.getInstance();
    private float p1 = 0.5f; //вероятность грузовых авто
    private float p2 = 0.5f; //вероятность пассажирских авто
    private int n1 = 2;
    private int n2 = 3;
    public int lifeTimeN1 = 5;
    public int lifeTimeN2 = 7;

    public void setTruckProbability(float probability) {
        this.p1 = probability;
    }
    public void setTruckTime(int time) {
        this.n1 = time;
    }

    public void setPassengerProbability(float probability) {
        this.p2 = probability;
    }
    public void setPassengerTime(int time) {
        this.n2 = time;
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
        Random rand = new Random();
        float p = rand.nextFloat();
        try {
            if ((newTime % n1 == 0) && (p1 >= p)) {
                number++;
                Truck truck = new Truck(rand.nextInt(0, width - 80), rand.nextInt(0, height - 180), rand.nextInt(Integer.MAX_VALUE), lifeTimeN1);
                carContainer.addCar(truck,time);
            }
            if ((newTime % n2 == 0) && (p2 >= p)) {
                number++;
                Passenger passenger = new Passenger(rand.nextInt(0, width - 80) , rand.nextInt(0, height - 180), rand.nextInt(Integer.MAX_VALUE), lifeTimeN2);
                carContainer.addCar(passenger,time);
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