package com.example.git.transports;

import com.example.git.management.IBehaviour;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;

public class Truck extends Transport implements IBehaviour {
    public static int intTruck = 0;
    private static Image img = new Image("police.png");
    public Truck(int x,int y) throws FileNotFoundException {
        super(x,y, img) ;
        intTruck++;
    }
}
