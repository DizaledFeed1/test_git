package com.example.git.server;

import com.example.git.management.CarContainer;
import com.example.git.management.HelloController;
import com.example.git.transports.Passenger;
import com.example.git.transports.Transport;
import com.example.git.transports.Truck;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private RadioButton passengerRadioButton,truckRadioButton;
    @FXML
    private ListView serverList;
    private ClientSomthing clientSomthing;
    String client;

    private HelloController helloController;

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }
    public void setServerList(ListView serverListMain){
        Platform.runLater(()->{
        serverList.setItems(serverListMain.getItems());
        });
    }
    public void setClientSomthing(ClientSomthing clientSomthing){
        this.clientSomthing = clientSomthing;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        passengerRadioButton.setToggleGroup(group);
        truckRadioButton.setToggleGroup(group);
        serverList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleClientSelection((String) newValue);
            }
        });
    }

    public void send(){
        CarContainer carContainer = CarContainer.getInstance();
        // Получаем список всех машин
        ArrayList<Transport> allCars = carContainer.getCarList();
        ArrayList<Transport> arrayCars = new ArrayList<>();
        if (truckRadioButton.isSelected()){
            for (Transport truck : allCars){
                if (truck instanceof Truck){
                    arrayCars.add(truck);
                }
            }
        } else {
            for (Transport passenger : allCars){
                if (passenger instanceof Passenger){
                    arrayCars.add(passenger);
                }
            }
        }
        clientSomthing.writeCars(arrayCars);
    }

    // Метод для обработки выбранного элемента
    private void handleClientSelection(String selectedClient) {
        // Здесь вы можете обработать выбранный элемент
        client = selectedClient;
        System.out.println("Выбранный клиент: " + selectedClient);
    }
}
