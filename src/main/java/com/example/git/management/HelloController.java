package com.example.git.management;

import com.example.git.AI.PassengerAI;
import com.example.git.AI.TruckAI;
import com.example.git.HelloApplication;
import com.example.git.transports.Passenger;
import com.example.git.transports.Transport;
import com.example.git.transports.Truck;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    Habitat habitat = new Habitat();
    private TruckAI truckAI;
    private PassengerAI passengerAI;
    private Console console;
    Timer timer;
    @FXML
    public Pane root, imgPane, modalPane,upPane,downPane;
    public long initializationTime;
    @FXML
    private Label timerLabel, textTimer;
    @FXML
    private Button startButton, stopButton,lookButton,truckOnButton,truckOffButton,passengerOnButton,passengerOffButton;
    @FXML
    private RadioButton open, close;
    @FXML
    private CheckBox CheckBoxMain;
    @FXML
    private CheckMenuItem CheckBoxMenu;
    @FXML
    private MenuItem MenuStartBtn, MenuStopBtn,consoleButtonMenu;
    @FXML
    private RadioMenuItem MenuRadioBtnHide, MenuRadioBtnShow;
    @FXML
    private ComboBox<String> passengerComboBox, truckComboBox,truckAiThread,passengerAiThread,mainAiThread;
    @FXML
    private TextField truckTextField, passengerTextField, lifeTimeTruck, lifeTimePassenger;
    long pauseTime = 0;

    @FXML
    public void menuBox() {
        CheckBoxMain.setSelected(CheckBoxMenu.isSelected());
    }
    @FXML
    public void mainBox() {
        CheckBoxMenu.setSelected(CheckBoxMain.isSelected());
    }
    @FXML
    public void currentObject() {
        long start = System.currentTimeMillis();
        timer.cancel();
        Statistic statistic = new Statistic(this);
        truckAI.pause();
        passengerAI.pause();
        statistic.openModalWindowLife(habitat.getCarContainer().getBirthTimeMap());
        pauseTime += System.currentTimeMillis() - start;
        start();
        isButtonAI();
    }
    @FXML
    public void check() {
        String truckInput = truckTextField.getText();
        String passengerInput = passengerTextField.getText();

        String truckInputTime = lifeTimeTruck.getText();
        String passengerInputTime = lifeTimePassenger.getText();

        try {
            int truckValue = Integer.parseInt(truckInput);
            int passengerValue = Integer.parseInt(passengerInput);

            int truckTimeLive = Integer.parseInt(truckInputTime);
            int passengerTimeLive = Integer.parseInt(passengerInputTime);

            if (truckValue == 0 || truckValue >= 100) {
                truckTextField.setText("1");
                throw new IllegalArgumentException();
            } else if (passengerValue == 0 || passengerValue >= 100) {
                passengerTextField.setText("1");
                throw new IllegalArgumentException();
            } else if (truckTimeLive == 0 || truckTimeLive >= 100) {
                lifeTimeTruck.setText("1");
                throw new IllegalArgumentException();
            } else if (passengerTimeLive == 0 || passengerTimeLive >= 100) {
                lifeTimePassenger.setText("1");
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            showAlert();
        }
    }
    @FXML
    public void handleNumericInput(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String text = textField.getText();
        if (!text.matches("\\d*")) {
            textField.setText(text.replaceAll("[^\\d]", ""));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        truckAI = new TruckAI(habitat.getCarContainer().getCarList());
        passengerAI = new PassengerAI(habitat.getCarContainer().getCarList());
        ToggleGroup group = new ToggleGroup();
        open.setToggleGroup(group);
        close.setToggleGroup(group);

        passengerComboBox.getItems().addAll("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        passengerComboBox.setValue("50"); // Установка начального значения
        truckComboBox.getItems().addAll("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        truckComboBox.setValue("50"); // Установка начального значения

        passengerAiThread.getItems().addAll("1","2","3","4","5","6","7","8","9","10");
        passengerAiThread.setValue("5");
        truckAiThread.getItems().addAll("1","2","3","4","5","6","7","8","9","10");
        truckAiThread.setValue("5");
        mainAiThread.getItems().addAll("1","2","3","4","5","6","7","8","9","10");
        mainAiThread.setValue("5");

        passengerComboBox.setOnAction(event -> {
            String selectedValue = passengerComboBox.getValue();
            habitat.setPassengerProbability(Float.parseFloat(selectedValue) / 100);
        });
        truckComboBox.setOnAction(event -> {
            String selectedValue = truckComboBox.getValue();
            habitat.setTruckProbability(Float.parseFloat(selectedValue) / 100);
        });
        truckAiThread.setOnAction(event ->{
            int selectedValue = Integer.parseInt(truckAiThread.getValue());
            truckAI.setPriority(selectedValue);
        });
        passengerAiThread.setOnAction(event ->{
            int selectedValue = Integer.parseInt(passengerAiThread.getValue());
            passengerAI.setPriority(selectedValue);
        });
        mainAiThread.setOnAction(event ->{
            int selectedValue = Integer.parseInt(mainAiThread.getValue());
            Thread.currentThread().setPriority(selectedValue);
        });
        MenuStartBtn.setOnAction(event -> {
            stopInitialize();
            start();
        });
        MenuStopBtn.setOnAction(event -> {
            pauseIntialize();
            if (CheckBoxMenu.isSelected()) {
                model();
            }
        });
        consoleButtonMenu.setOnAction(event ->{
            try {
                getConsole();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        open.setOnAction(event -> {
            swapTimer();
            MenuRadioBtnShow.setSelected(true);
        });
        close.setOnAction(event -> {
            swapTimer();
            MenuRadioBtnHide.setSelected(true);
        });
        MenuRadioBtnShow.setOnAction(event -> {
            swapTimer();
            open.setSelected(true);
        });
        MenuRadioBtnHide.setOnAction(event -> {
            swapTimer();
            close.setSelected(true);
        });
        startButton.setOnAction(event -> {
            stopInitialize();
            start();
        });
        stopButton.setOnAction(event -> {
            pauseIntialize();
            if (CheckBoxMain.isSelected()) {
                model();
            }
        });
        truckOnButton.setOnAction(event ->{
            swapAIButton(1);
                truckAI.resumeAI();

        });
        truckOffButton.setOnAction(event -> {
            swapAIButton(1);
            truckAI.pause();
        });
        passengerOnButton.setOnAction(event ->{
            swapAIButton(2);
            passengerAI.resumeAI();
        });
        passengerOffButton.setOnAction(event ->{
            swapAIButton(2);
            passengerAI.pause();
        });
    }

    private void getConsole() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("console.fxml"));

        Stage consoleStage = new Stage();
        Scene consoleScene = new Scene(fxmlLoader.load());

        console = fxmlLoader.getController();
        console.setHelloController(this);

        consoleStage.setTitle("Консоль");
        consoleStage.setMinWidth(300);
        consoleStage.setMinHeight(300);

        consoleStage.setScene(consoleScene);
        consoleStage.show();
    }

    private void swapAIButton(int number){
        switch (number){
            case 1:{
                truckOnButton.setDisable(!(truckOnButton.isDisable()));
                truckOffButton.setDisable(!(truckOffButton.isDisable()));
                break;
            }
            case 2:{
                passengerOnButton.setDisable(!(passengerOnButton.isDisabled()));
                passengerOffButton.setDisable(!(passengerOffButton.isDisabled()));
                break;
            }
        }
    }
    public void onKey() {
        imgPane.getScene().setOnKeyReleased((KeyEvent event) -> {
            switch (event.getCode()) {
                case B -> {
                    if (!startButton.isDisabled()) {
                        stopInitialize();
                        start();
                    }
                }
                case E -> {
                    if (!stopButton.isDisabled()) {
                        pauseIntialize();
                        if (CheckBoxMain.isSelected()) {
                            model();
                        }
                    }
                }
                case T -> {
                    timerLabel.setVisible(!timerLabel.isVisible());
                    textTimer.setVisible(!textTimer.isVisible());
                    if (open.isSelected()) {
                        close.setSelected(!close.isSelected());
                        MenuRadioBtnHide.setSelected(!MenuRadioBtnHide.isSelected());
                    } else {
                        open.setSelected(!open.isSelected());
                        MenuRadioBtnShow.setSelected(!MenuRadioBtnShow.isSelected());
                    }
                    open.setDisable(!open.isDisabled());
                    close.setDisable(!close.isDisabled());

                    MenuRadioBtnShow.setDisable(!MenuRadioBtnShow.isDisable());
                    MenuRadioBtnHide.setDisable(!MenuRadioBtnHide.isDisable());
                }
            }
        });
    }

    private void swapTimer() {
        timerLabel.setVisible(!timerLabel.isVisible());
        textTimer.setVisible(!textTimer.isVisible());

        MenuRadioBtnShow.setDisable(!(MenuRadioBtnShow.isDisable()));
        MenuRadioBtnHide.setDisable(!(MenuRadioBtnHide.isDisable()));

        open.setDisable(!(open.isDisabled()));
        close.setDisable(!(close.isDisabled()));
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Неверное значение");
        alert.setContentText("Значения должны быть цифрой, которая больше 0 и не превышать 100");
        alert.showAndWait();
    }
    private void isButtonAI(){
        if (passengerOnButton.isDisabled()){
            passengerAI.resumeAI();
        }
        if (truckOnButton.isDisabled()){
            truckAI.resumeAI();
        }
    }
    private void startTimer() {
        long time = System.currentTimeMillis() - initializationTime - pauseTime;
        timerLabel.setText(String.valueOf(time / 1000));
    }
    public void start() {
        if (!startButton.isDisabled()) {
            swapDisable();
        }
        isButtonAI();
        lookButton.setDisable(false);
        habitat.setTruckTime(Integer.parseInt(truckTextField.getText()));
        habitat.setPassengerTime(Integer.parseInt(passengerTextField.getText()));
        habitat.setLifeTimeN1(Integer.parseInt(lifeTimeTruck.getText()));
        habitat.setLifeTimeN2(Integer.parseInt(lifeTimePassenger.getText()));
        if (initializationTime == 0) {
            initializationTime = System.currentTimeMillis();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    startTimer();
                    try {
                        int number = habitat.Update( System.currentTimeMillis() - initializationTime);
                        CarContainer carContainer = habitat.getCarContainer();
                        if (number == 1){
                            imgPane.getChildren().add(carContainer.getCarList().get(carContainer.getCarList().size()-1).getImageView());
                        }
                        else if (number == 2) {
                            imgPane.getChildren().add(carContainer.getCarList().get(carContainer.getCarList().size()-1).getImageView());
                            imgPane.getChildren().add(carContainer.getCarList().get(carContainer.getCarList().size()-2).getImageView());
                        }
                        checkDeath(carContainer);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000);
    }
    private void checkDeath(CarContainer carContainer) {
        if (!(carContainer.getBirthTimeMap().isEmpty())) {
            long currentTime = System.currentTimeMillis(); // Текущее время
            Iterator<Transport> iterator = carContainer.getCarList().iterator();
            while (iterator.hasNext()) {
                Transport transport = iterator.next();
                if (transport instanceof Truck) {
                    Truck truck = (Truck) transport; // Приведение к типу Truck
                    if (truck.isLifeTimeExpired(currentTime, habitat.lifeTimeN1,pauseTime)) {
                        imgPane.getChildren().remove(truck.getImageView());
                        iterator.remove(); // Удаление из списка
                        carContainer.getBirthTimeMap().remove(truck.getId());
                        carContainer.getIdSet().remove(truck.getId());
                        System.out.println("Удалили грузовик");
                    }
                } else if (transport instanceof Passenger) {
                    Passenger passenger = (Passenger) transport;
                    if (passenger.isLifeTimeExpired(currentTime, habitat.lifeTimeN2,pauseTime)) {
                        imgPane.getChildren().remove(passenger.getImageView());
                        iterator.remove(); // Удаление из списка
                        carContainer.getBirthTimeMap().remove(passenger.getId());
                        carContainer.getIdSet().remove(passenger.getId());
                    }
                } else {
                    System.out.println("Ничего не удаляем");
                }
            }
        }
    }
    private void pauseIntialize() {
        swapDisable();
        timer.cancel();
        truckAI.pause();
        passengerAI.pause();
    }

    private void swapDisable() {
        startButton.setDisable(!(startButton.isDisabled()));
        stopButton.setDisable(!(stopButton.isDisabled()));

        MenuStartBtn.setDisable(startButton.isDisabled());
        MenuStopBtn.setDisable(stopButton.isDisabled());

        truckTextField.setDisable(!(truckTextField.isDisabled()));
        lifeTimeTruck.setDisable(!(lifeTimeTruck.isDisabled()));
        truckComboBox.setDisable(!(truckComboBox.isDisabled()));

        passengerTextField.setDisable(!(passengerTextField.isDisabled()));
        lifeTimePassenger.setDisable(!(lifeTimePassenger.isDisabled()));
        passengerComboBox.setDisable(!(passengerComboBox.isDisabled()));
    }

    public void stopInitialize() {
        if (timer != null) {
            timer.cancel();
            pauseTime = 0;
            timer = null;
            initializationTime = 0;
            Passenger.intPassenger = 0;
            Truck.intTruck = 0;
            CarContainer carContainer = habitat.getCarContainer();
            carContainer.getCarList().forEach((tmp) -> imgPane.getChildren().remove(tmp.getImageView()));
            habitat.getCarContainer().getCarList().clear();
            habitat.getCarContainer().getIdSet().clear();
            habitat.getCarContainer().getBirthTimeMap().clear();
            carContainer.getCarList().clear();
        }
    }

    public TruckAI getTruckAI(){
        return truckAI;
    }
    public PassengerAI getPassengerAI(){
        return passengerAI;
    }
    public void swapAI(int number){
        if (number == 1) {
            passengerOnButton.setDisable(false);
            passengerOffButton.setDisable(true);

            truckOnButton.setDisable(false);
            truckOffButton.setDisable(true);
        }else {
            passengerOnButton.setDisable(true);
            passengerOffButton.setDisable(false);

            truckOnButton.setDisable(true);
            truckOffButton.setDisable(false);
        }
    }

    public void model() {
        long start = System.currentTimeMillis();
        Statistic statistic = new Statistic(this);
        statistic.openModalWindow(start);
        pauseTime += System.currentTimeMillis() - start;
    }
}
