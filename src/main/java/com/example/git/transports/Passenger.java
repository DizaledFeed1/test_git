
package com.example.git.transports;

import com.example.git.management.IBehaviour;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;



public class Passenger extends Transport implements IBehaviour {
    private long creationTime;
    public static int intPassenger = 0;
    private static Image img = new Image("Car.png");

    public Passenger(int x,int y,int id, long lifetime) throws FileNotFoundException {
        super(x,y, img, id, lifetime);
        creationTime = System.currentTimeMillis(); // Запоминаем время создания
        intPassenger++;
    }
    public boolean isLifeTimeExpired(long currentTime, long lifeTime,long pauseTime) {
        long elapsedTime = currentTime - creationTime - pauseTime;
        return elapsedTime / 1000 >= lifeTime;
    }

    // Метод для получения времени создания (для тестирования)
    public long getCreationTime() {
        return creationTime;
    }
}
