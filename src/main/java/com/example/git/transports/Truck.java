package com.example.git.transports;

import com.example.git.management.IBehaviour;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;

public class Truck extends Transport implements IBehaviour {
    private long creationTime;
    public static int intTruck =0;

    public Truck(int x, int y, int id, long lifetime) throws FileNotFoundException {
        super(x, y, new Image("police.png"), id, lifetime);
        creationTime = System.currentTimeMillis(); // Запоминаем время создания
        intTruck ++;
    }

    // Метод для проверки истечения времени жизни
    public boolean isLifeTimeExpired(long currentTime, long lifeTime,long pauseTime) {
        long elapsedTime = currentTime - creationTime - pauseTime;
        return elapsedTime / 1000 >= lifeTime;
    }

    // Метод для получения времени создания (для тестирования)
    public long getCreationTime() {
        return creationTime;
    }
}
