package com.example.git.management;
import com.example.git.transports.Transport;

import java.util.ArrayList;

public class CarContainer {
    private static CarContainer instance;
    private ArrayList<Transport> carList;

    private CarContainer() {
        carList = new ArrayList<>();
    }

    public static CarContainer getInstance() {
        if (instance == null) {
            instance = new CarContainer();
        }
        return instance;
    }

    public void addCar(Transport transport) {
        carList.add(transport);
    }

    public ArrayList<Transport> getCarList() {
        return carList;
    }
}
