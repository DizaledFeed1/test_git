package com.example.git.management;

import com.example.git.transports.Passenger;
import com.example.git.transports.Transport;
import com.example.git.transports.Truck;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController implements Initializable {
    Habitat habitat = new Habitat();
    Timer timer;
    @FXML
    public Pane root,imgPane;
    public long initializationTime;
    @FXML
    private Label timerLabel,textTimer;
    @FXML
    private Button startButton,stopButton;
    @FXML
    private RadioButton open,close;
    @FXML
    private CheckBox CheckBoxMain;
    @FXML
    private CheckMenuItem CheckBoxMenu;
    @FXML
    private MenuItem MenuStartBtn, MenuStopBtn;
    @FXML
    private RadioMenuItem MenuRadioBtnHide,MenuRadioBtnShow;
    @FXML
    private ComboBox<String> passengerComboBox,truckComboBox;
    @FXML
    private TextField truckTextField,passengerTextField,passengerText,truckText;
    long pauseTime = 0;

    @FXML
    public void menuBox () {
        CheckBoxMain.setSelected(CheckBoxMenu.isSelected());
    }
    @FXML
    public void mainBox() {
        CheckBoxMenu.setSelected(CheckBoxMain.isSelected());
    }
    @FXML
    public void check(){
        String truckInput = truckTextField.getText();
        String passengerInput = passengerTextField.getText();

        String truckInputTime = truckText.getText();
        String passengerInputTime = passengerText.getText();

        try {
            int truckValue = Integer.parseInt(truckInput);
            int passengerValue = Integer.parseInt(passengerInput);

            int truckTimeLive = Integer.parseInt(truckInputTime);
            int passengerTimeLive = Integer.parseInt(passengerInputTime);

            if (truckValue == 0 || truckValue > 100) {
                truckTextField.setText("1");
                throw new IllegalArgumentException();
            } else if (passengerValue == 0 || passengerValue > 100) {
                passengerTextField.setText("1");
                throw new IllegalArgumentException();
            } else if (truckTimeLive == 0 || truckTimeLive > 100) {
                truckText.setText("1");
                throw new IllegalArgumentException();
            } else if (passengerTimeLive == 0 || passengerTimeLive > 100) {
                passengerText.setText("1");
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
        ToggleGroup group = new ToggleGroup();
        open.setToggleGroup(group);
        close.setToggleGroup(group);

        passengerComboBox.getItems().addAll("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        passengerComboBox.setValue("50"); // Установка начального значения
        truckComboBox.getItems().addAll("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        truckComboBox.setValue("50"); // Установка начального значения

        passengerComboBox.setOnAction(event -> {
            String selectedValue = passengerComboBox.getValue();
            habitat.setPassengerProbability(Float.parseFloat(selectedValue) / 100);
        });
        truckComboBox.setOnAction(event -> {
            String selectedValue = truckComboBox.getValue();
            habitat.setTruckProbability(Float.parseFloat(selectedValue) / 100);
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
        startButton.setOnAction(event ->{
            stopInitialize();
            start();
        });
        stopButton.setOnAction(event ->{
            pauseIntialize();
            if (CheckBoxMain.isSelected()) {
                model();
            }
        });
    }
    public void onKey(){
        imgPane.getScene().setOnKeyReleased((KeyEvent event) ->{
            switch (event.getCode()){
                case B -> {
                    if (startButton.isDisabled() == false) {
                        stopInitialize();
                        start();
                    }
                }
                case E -> {
                    if (stopButton.isDisabled() == false) {
                        pauseIntialize();
                        if (CheckBoxMain.isSelected()) {
                            model();
                        }
                    }
                }
                case T -> {
                    timerLabel.setVisible(!timerLabel.isVisible());
                    textTimer.setVisible(!textTimer.isVisible());
                    if (open.isSelected()){
                        close.setSelected(!close.isSelected());
                        MenuRadioBtnHide.setSelected(!MenuRadioBtnHide.isSelected());
                    }
                    else {
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
    private void swapTimer(){
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
    private void startTimer(){
        long time = System.currentTimeMillis() - initializationTime - pauseTime;
        timerLabel.setText(String.valueOf(time / 1000));
    }
    public void start() {
        swapDisable();
        habitat.setTruckTime(Integer.parseInt(truckTextField.getText()));
        habitat.setPassengerTime(Integer.parseInt(passengerTextField.getText()));
            if(initializationTime == 0) {
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
            long currentTime = (System.currentTimeMillis() - initializationTime) / 1000;
            for (Transport transport : carContainer.getCarList()) {
                int id = transport.getId();
                long birthTime = carContainer.getBirthTimeMap().get(id) / 1000;
                long lifeTime = currentTime - birthTime;

                if (transport instanceof Truck) {
                    if (lifeTime >= habitat.lifeTimeN1) {
                        imgPane.getChildren().remove(transport.getImageView());
                        carContainer.getCarList().remove(transport);
                        carContainer.getBirthTimeMap().remove(id);
                        carContainer.getIdSet().remove(id);
                        System.out.println("Удалили грузовик");
                    }
                } else if (transport instanceof Passenger) {
                    if (lifeTime >= habitat.lifeTimeN2) {
                        imgPane.getChildren().remove(transport.getImageView());
                        carContainer.getCarList().remove(transport);
                        carContainer.getBirthTimeMap().remove(id);
                        carContainer.getIdSet().remove(id);
                        System.out.println("Удалили легковушку");
                    }
                } else System.out.println("Ничего не удаляем");
            }
        }
    }
    private void pauseIntialize() {
        swapDisable();
        timer.cancel();
    }
    private  void swapDisable(){
        startButton.setDisable(!(startButton.isDisabled()));
        stopButton.setDisable(!(stopButton.isDisabled()));

        MenuStartBtn.setDisable(startButton.isDisabled());
        MenuStopBtn.setDisable(stopButton.isDisabled());

        truckTextField.setDisable(!(truckTextField.isDisabled()));
        truckText.setDisable(!(truckText.isDisabled()));
        truckComboBox.setDisable(!(truckComboBox.isDisabled()));

        passengerTextField.setDisable(!(passengerTextField.isDisabled()));
        passengerText.setDisable(!(passengerText.isDisabled()));
        passengerComboBox.setDisable(!(passengerComboBox.isDisabled()));
    }
    public void stopInitialize()
    {
        if (timer != null) {
            timer.cancel();
            pauseTime = 0;
            timer = null;
            initializationTime = 0;
            Passenger.intPassenger = 0;
            Truck.intTruck = 0;
            CarContainer carContainer = habitat.getCarContainer();
            carContainer.getCarList().forEach((tmp) -> imgPane.getChildren().remove(tmp.getImageView()));
            carContainer.getCarList().clear();
        }
    }
    public void model(){
        long start = System.currentTimeMillis();
        Statistic statistic = new Statistic(this);
        statistic.openModalWindow(start);
        pauseTime += System.currentTimeMillis() - start;
    }
}