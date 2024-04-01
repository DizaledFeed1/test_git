package com.example.git.management;

import com.example.git.transports.Passenger;
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
    public Pane root;
    @FXML
    public Pane imgPane;
    public long initializationTime;
    @FXML
    private Label timerLabel;
    @FXML
    private Label textTimer;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private RadioButton open;
    @FXML
    private RadioButton close;
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
    private TextField truckTextField,passengerTextField;

    boolean openFlag = true;
    boolean closeFlag = false;
    boolean startFlag = false;
    boolean finishFlag = false;
    long pauseTime = 0;
    @FXML
    public void model(){
        long start = System.currentTimeMillis();
        Statistic statistic = new Statistic(this);
        statistic.openModalWindow(start);
        pauseTime += System.currentTimeMillis() - start;
    }
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

        try {
            int truckValue = Integer.parseInt(truckInput);
            int passengerValue = Integer.parseInt(passengerInput);

            if (truckValue == 0 || truckValue > 100) {
                truckTextField.setText("1");
                throw new IllegalArgumentException();
            } else if (passengerValue == 0 || passengerValue > 100) {
                passengerTextField.setText("1");
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            showAlert();
        }
    }
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Неверное значение");
        alert.setContentText("Значения должны быть цифрой, которая больше 0 и не превышать 100");
        alert.showAndWait();
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
        open.setSelected(true);
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
        close.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timerLabel.setVisible(!timerLabel.isVisible());
                textTimer.setVisible(!textTimer.isVisible());
                MenuRadioBtnHide.setSelected(true);
                MenuRadioBtnShow.setDisable(false);
                open.setDisable(false);
                MenuRadioBtnHide.setDisable(true);
                close.setDisable(true);
            }
        });
        open.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                    timerLabel.setVisible(!timerLabel.isVisible());
                    textTimer.setVisible(!textTimer.isVisible());
                    MenuRadioBtnShow.setSelected(true);
                    open.setSelected(true);
                    MenuRadioBtnShow.setDisable(true);
                    open.setDisable(true);
                    MenuRadioBtnHide.setDisable(false);
                    close.setDisable(false);
            }
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
        MenuRadioBtnShow.setOnAction(event -> {
                timerLabel.setVisible(!timerLabel.isVisible());
                textTimer.setVisible(!textTimer.isVisible());
                open.setSelected(true);
                MenuRadioBtnShow.setDisable(true);
                open.setDisable(true);
                MenuRadioBtnHide.setDisable(false);
                close.setDisable(false);
        });
        MenuRadioBtnHide.setOnAction(event -> {
                timerLabel.setVisible(!timerLabel.isVisible());
                textTimer.setVisible(!textTimer.isVisible());
                close.setSelected(true);
                MenuRadioBtnShow.setDisable(false);
                open.setDisable(false);
                MenuRadioBtnHide.setDisable(true);
                close.setDisable(true);
        });
        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startFlag = true;
                finishFlag =false;
                stopInitialize();
                start();
            }
        });
        stopButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startFlag = false;
                pauseIntialize();
                if (CheckBoxMain.isSelected()) {
                    model();
                }
            }

        });
    }
    public HelloController() {
        super();
    }
    public void onKey(){
        imgPane.getScene().setOnKeyReleased((KeyEvent event) ->{
            switch (event.getCode()){
                case B -> {
                    if (startFlag ==false) {
                        startFlag = true;
                        finishFlag =false;
                        stopInitialize();
                        start();
                    }
                }
                case E -> {
                    if (finishFlag == false) {
                        startFlag = false;
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
    private void startTimer(){
        long time = System.currentTimeMillis() - initializationTime - pauseTime;
        timerLabel.setText(String.valueOf(time / 1000));
    }
    public void start() {
        truckTextField.setDisable(true);
        passengerTextField.setDisable(true);
        truckComboBox.setDisable(true);
        passengerComboBox.setDisable(true);
        startButton.setDisable(true);
        stopButton.setDisable(false);
        MenuRadioBtnShow.setDisable(true);
        MenuRadioBtnShow.setSelected(true);
        MenuRadioBtnHide.setDisable(false);
        open.setSelected(true);
        close.setSelected(false);
        openFlag = true;
        closeFlag = false;
        close.setDisable(false);
        open.setDisable(true);
        timerLabel.setVisible(true);
        textTimer.setVisible(true);
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
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }, 0, 1000);
        }
    private void pauseIntialize() {
        truckTextField.setDisable(false);
        passengerTextField.setDisable(false);
        truckComboBox.setDisable(false);
        passengerComboBox.setDisable(false);
        stopButton.setDisable(true); // Отключаем кнопку "стоп"
        startButton.setDisable(false); // Включаем кнопку "старт"

        MenuStartBtn.setDisable(false);
        MenuStopBtn.setDisable(true);

        MenuRadioBtnHide.setDisable(false);
        MenuRadioBtnHide.setSelected(false);
        MenuRadioBtnShow.setDisable(true);
        close.setSelected(false);
        close.setDisable(false);
        open.setDisable(true);
        timer.cancel();
    }
    public void stopInitialize()
    {
        stopButton.setDisable(false);
        MenuStopBtn.setDisable(false);
        startButton.setDisable(true);
        MenuStartBtn.setDisable(true);
        if (timer != null) {
            closeFlag = true;
            openFlag = false;
            close.setSelected(true);
            open.setDisable(true);
            timer.cancel();
            timer = null;
            initializationTime =0;
            Passenger.intPassenger =0;
            Truck.intTruck = 0;
            CarContainer carContainer = habitat.getCarContainer();
            carContainer.getCarList().forEach((tmp) -> imgPane.getChildren().remove(tmp.getImageView()));
            carContainer.getCarList().clear();
        }
    }
}