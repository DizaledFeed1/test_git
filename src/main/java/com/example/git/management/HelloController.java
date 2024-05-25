package com.example.git.management;

import com.example.git.AI.PassengerAI;
import com.example.git.AI.TruckAI;
import com.example.git.HelloApplication;


import com.example.git.server.ClientSomthing;
import com.example.git.server.TCPServer;
import com.example.git.transports.Passenger;
import com.example.git.transports.Transport;
import com.example.git.transports.Truck;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.*;


public class HelloController implements Initializable {
    Habitat habitat = new Habitat();
    private TruckAI truckAI;
    private PassengerAI passengerAI;
    private Console console;
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    Timer timer;
    @FXML
    public Pane root, imgPane, modalPane, upPane, downPane;
    public long initializationTime;
    @FXML
    private Label timerLabel, textTimer;
    @FXML
    private Button startButton, stopButton, lookButton, truckOnButton, truckOffButton, passengerOnButton, passengerOffButton,connectButton,disconnectButton;
    @FXML
    private RadioButton open, close;
    @FXML
    private CheckBox CheckBoxMain;
    @FXML
    private CheckMenuItem CheckBoxMenu;
    @FXML
    private MenuItem MenuStartBtn, MenuStopBtn, consoleButtonMenu;
    @FXML
    private RadioMenuItem MenuRadioBtnHide, MenuRadioBtnShow;
    @FXML
    private ComboBox<String> passengerComboBox, truckComboBox, truckAiThread, passengerAiThread, mainAiThread;
    @FXML
    private TextField truckTextField, passengerTextField, lifeTimeTruck, lifeTimePassenger;
    @FXML
    private ListView<String> serverList;
    private  ClientSomthing clientSomthing;
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
        saveConfig();
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
        disconnectButton.setDisable(true);

        truckAI = new TruckAI(habitat.getCarContainer().getCarList());
        passengerAI = new PassengerAI(habitat.getCarContainer().getCarList());
        ToggleGroup group = new ToggleGroup();
        open.setToggleGroup(group);
        close.setToggleGroup(group);

        passengerComboBox.getItems().addAll("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        passengerComboBox.setValue("50"); // Установка начального значения
        truckComboBox.getItems().addAll("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");


        passengerAiThread.getItems().addAll("1","2","3","4","5","6","7","8","9","10");
        truckAiThread.getItems().addAll("1","2","3","4","5","6","7","8","9","10");
        mainAiThread.getItems().addAll("1","2","3","4","5","6","7","8","9","10");
        downloadConfig();
        if (open.isSelected()){
            timerLabel.setVisible(true);
            textTimer.setVisible(true);
        }
        else {
            timerLabel.setVisible(false);
            textTimer.setVisible(false);
        }

        passengerComboBox.setOnAction(event -> {
            String selectedValue = passengerComboBox.getValue();
            habitat.setPassengerProbability(Float.parseFloat(selectedValue) / 100);
            saveConfig();
        });
        truckComboBox.setOnAction(event -> {
            String selectedValue = truckComboBox.getValue();
            habitat.setTruckProbability(Float.parseFloat(selectedValue) / 100);
            saveConfig();
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
            saveConfig();
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
            saveConfig();
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
    public void connect(){
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        String ipAddr = "localhost";
        int port = 8080;
        new Thread(() -> {
            try {
                clientSomthing = new ClientSomthing(ipAddr, port);
            } catch (Exception e) {
                e.printStackTrace();
                // В случае ошибки, нужно обновить UI в потоке JavaFX
                javafx.application.Platform.runLater(() -> {
                    connectButton.setDisable(false);
                    disconnectButton.setDisable(true);
                });
            }
        }).start();
    }
    public void disconnect(){
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
        clientSomthing.disconnect();
    }

    private void setText (String serverWord){
        // Разделяем строку на слова по пробелам
        String[] words = serverWord.split(" ");

        // Создаем ObservableList и добавляем в него слова
        ObservableList<String> items = FXCollections.observableArrayList(words);

        // Устанавливаем элементы в ListView
        serverList.setItems(items);
    }
    private void saveConfig() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"))) {
            writer.write("probabilityTruck=" + habitat.getTruckProbability());
            writer.newLine();
            writer.write("probabilityPassenger=" + habitat.getPassengerProbability());
            writer.newLine();
            writer.write("truckTextField=" + habitat.getN1());
            writer.newLine();
            writer.write("passengerTextField=" + habitat.getN2());
            writer.newLine();
            writer.write("lifeTimeTruck=" + habitat.getLifeTimeN1());
            writer.newLine();
            writer.write("lifeTimePassenger=" + habitat.getLifeTimeN2());
            writer.newLine();
            writer.write("truckAiThread=" + truckAiThread.getValue());
            writer.newLine();
            writer.write("passengerAiThread=" + passengerAiThread.getValue());
            writer.newLine();
            writer.write("mainAiThread=" + mainAiThread.getValue());
            writer.newLine();
            writer.write("checkBoxMenu=" + CheckBoxMenu.isSelected());
            writer.newLine();
            writer.write("truckOnButton=" + truckOnButton.isDisable());
            writer.newLine();
            writer.write("truckOffButton=" + truckOffButton.isDisable());
            writer.newLine();
            writer.write("passengerOnButton=" + passengerOnButton.isDisable());
            writer.newLine();
            writer.write("passengerOffButton=" + passengerOffButton.isDisable());
            writer.newLine();
            writer.write("open=" + open.isSelected());
            writer.newLine();
            writer.write("openDisabled=" + open.isDisabled());
            writer.newLine();
            writer.write("close=" + close.isSelected());
            writer.newLine();
            writer.write("closeDisabled=" + close.isDisabled());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                String key = parts[0];
                String value = parts[1];
                switch (key) {
                    case "probabilityTruck":
                        habitat.setTruckProbability(Float.parseFloat(value));
                        truckComboBox.setValue(String.valueOf((int) (habitat.getTruckProbability() * 100)));
                        break;
                    case "probabilityPassenger":
                        habitat.setPassengerProbability(Float.parseFloat(value));
                        passengerComboBox.setValue(String.valueOf((int) (habitat.getPassengerProbability() * 100)));
                        break;
                    case "truckTextField":
                        habitat.setTruckTime(Integer.parseInt(value));
                        truckTextField.setText(value);
                        break;
                    case "passengerTextField":
                        habitat.setPassengerTime(Integer.parseInt(value));
                        passengerTextField.setText(value);
                        break;
                    case "lifeTimeTruck":
                        habitat.setLifeTimeN1(Integer.parseInt(value));
                        lifeTimeTruck.setText(value);
                        break;
                    case "lifeTimePassenger":
                        habitat.setLifeTimeN2(Integer.parseInt(value));
                        lifeTimePassenger.setText(value);
                        break;
                    case "truckAiThread":
                        truckAI.setPriority(Integer.parseInt(value));
                        truckAiThread.setValue(value);
                        break;
                    case "passengerAiThread":
                        passengerAI.setPriority(Integer.parseInt(value));
                        passengerAiThread.setValue(value);
                        break;
                    case "mainAiThread":
                        Thread.currentThread().setPriority(Integer.parseInt(value));
                        mainAiThread.setValue(value);
                        break;
                    case "checkBoxMenu":
                        CheckBoxMenu.setSelected(Boolean.parseBoolean(value));
                        CheckBoxMain.setSelected(Boolean.parseBoolean(value));
                        break;
                    case "truckOnButton":
                        truckOnButton.setDisable(Boolean.parseBoolean(value));
                        break;
                    case "truckOffButton":
                        truckOffButton.setDisable(Boolean.parseBoolean(value));
                        break;
                    case "passengerOnButton":
                        passengerOnButton.setDisable(Boolean.parseBoolean(value));
                        break;
                    case "passengerOffButton":
                        passengerOffButton.setDisable(Boolean.parseBoolean(value));
                        break;
                    case "open":
                        open.setSelected(Boolean.parseBoolean(value));
                        MenuRadioBtnShow.setSelected(Boolean.parseBoolean(value));
                        break;
                    case "openDisabled":
                        open.setDisable(Boolean.parseBoolean(value));
                        MenuRadioBtnShow.setDisable(Boolean.parseBoolean(value));
                        break;
                    case "close":
                        close.setSelected(Boolean.parseBoolean(value));
                        MenuRadioBtnHide.setSelected(Boolean.parseBoolean(value));
                        break;
                    case "closeDisabled":
                        close.setDisable(Boolean.parseBoolean(value));
                        MenuRadioBtnHide.setDisable(Boolean.parseBoolean(value));
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCarData() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выбор пути сохранения");

            // Указываем расширение по умолчанию и фильтр файлов
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized files (*.ser)", "*.ser");
            fileChooser.getExtensionFilters().add(extFilter);

            // Показываем диалог сохранения файла и получаем выбранный файл
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(habitat.getCarContainer());
                out.close();
                fileOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void dowloadCarData(){
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Car Data File");
            File file = fileChooser.showOpenDialog(null);

            if (file !=null) {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                CarContainer loadedContainer = (CarContainer) in.readObject();
                in.close();
                fileIn.close();

                ArrayList<Transport> carList = loadedContainer.getCarList();
                for (Transport transport : carList) {
                    if (transport instanceof Truck) {
                        Truck truck = new Truck(transport.getX(), transport.getY(), transport.getFinalX(), transport.getFinalY(), transport.getId(), transport.getLifetime());
                        long currentTime = System.currentTimeMillis() - pauseTime;
                        if (initializationTime == 0) {
                            currentTime = 0;
                        }
                        truck.setCreationTime(currentTime);
                        currentTime = currentTime - initializationTime;
                        habitat.getCarContainer().addCar(truck, currentTime / 1000);
                        imgPane.getChildren().add(habitat.getCarContainer().getCarList().get(habitat.getCarContainer().getCarList().size() - 1).getImageView());
                    } else if (transport instanceof Passenger) {
                        Passenger passenger = new Passenger(transport.getX(), transport.getY(), transport.getFinalX(), transport.getFinalY(), transport.getId(), transport.getLifetime());
                        long currentTime = System.currentTimeMillis() - pauseTime;
                        if (initializationTime == 0) {
                            currentTime = 0;
                        }
                        passenger.setCreationTime(currentTime);
                        currentTime = currentTime - initializationTime;
                        habitat.getCarContainer().addCar(passenger, currentTime / 1000);
                        imgPane.getChildren().add(habitat.getCarContainer().getCarList().get(habitat.getCarContainer().getCarList().size() - 1).getImageView());
                    }
                }
            }
            System.out.println("контейнер заменён");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        currentObject();
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
    private void checkStart(){
        if (!startButton.isDisabled()) {
            swapDisable();
        }
        if (passengerOffButton.isDisable()){
            passengerAI.pause();
        }if (truckOffButton.isDisable()){
            truckAI.pause();
        }
        isButtonAI();
    }
    public void start() {
        checkStart();
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