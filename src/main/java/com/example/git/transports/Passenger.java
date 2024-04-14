package com.example.git.transports;

import com.example.git.management.IBehaviour;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.util.Random;


public class Passenger extends Transport implements IBehaviour {
    private long creationTime;
    public static int intPassenger = 0;
    public static Random rand = new Random();
    private static Image img = new Image("Car.png");

    public Passenger(int x,int y,int finalyX,int finalyY,int id, long lifetime) throws FileNotFoundException {
        super(x,y, finalyX,finalyY, img, id, lifetime);
        creationTime = System.currentTimeMillis(); // Запоминаем время создания
        intPassenger++;
    }
    public boolean isLifeTimeExpired(long currentTime, long lifeTime,long pauseTime) {
        long elapsedTime = currentTime - creationTime - pauseTime;
        return elapsedTime / 1000 >= lifeTime;
    }

}
