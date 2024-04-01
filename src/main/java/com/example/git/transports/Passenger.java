
package com.example.git.transports;

import com.example.git.management.IBehaviour;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;



public class Passenger extends Transport implements IBehaviour {

    public static int intPassenger = 0;
    private static Image img = new Image("Car.png");

    public Passenger(int x,int y,int id,long birthTime, long lifetime) throws FileNotFoundException {
        super(x,y, img, id, birthTime, lifetime);
        intPassenger++;
    }
}
