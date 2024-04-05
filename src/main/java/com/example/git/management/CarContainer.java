package com.example.git.management;
import com.example.git.transports.Transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
public class CarContainer {
    private static CarContainer instance;
    private ArrayList<Transport> carList;
    private TreeSet<Integer> idSet;
    private HashMap<Long, Integer> birthTimeMap;

    private CarContainer() {
        carList = new ArrayList<>();
        idSet = new TreeSet<>();
        birthTimeMap = new HashMap<>();
    }

    public static CarContainer getInstance() {
        if (instance == null) {
            instance = new CarContainer();
        }
        return instance;
    }

    public void addCar(Transport transport,long birthTime) {
        carList.add(transport);
        int id = transport.getId();
        idSet.add(id);
        birthTimeMap.put(birthTime,id);
    }

    public ArrayList<Transport> getCarList() {
        return carList;
    }
    public HashMap<Long, Integer> getBirthTimeMap(){
        return  birthTimeMap;
    }
    public TreeSet<Integer> getIdSet(){
        return  idSet;
    }
}
