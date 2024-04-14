package com.example.git.transports;

import com.example.git.management.IBehaviour;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.util.Random;

public class Truck extends Transport implements IBehaviour {
    private long creationTime;
    public static int intTruck =0;
    public static Random rand = new Random();
    private static Image img = new Image("police.png");

    public Truck(int x, int y,int finalyX,int finalyY, int id, long lifetime) throws FileNotFoundException {
        super(x, y,finalyX,finalyY, img, id, lifetime);
        creationTime = System.currentTimeMillis(); // Запоминаем время создания
        intTruck ++;
    }

    // Метод для проверки истечения времени жизни
    public boolean isLifeTimeExpired(long currentTime, long lifeTime,long pauseTime) {
        long elapsedTime = currentTime - creationTime - pauseTime;
        return elapsedTime / 1000 >= lifeTime;
    }
}
