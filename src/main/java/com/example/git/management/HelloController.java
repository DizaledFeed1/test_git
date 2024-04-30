package com.example.git.management;

import com.example.git.AI.PassengerAI;
import com.example.git.AI.TruckAI;
import com.example.git.HelloApplication;


import com.example.git.server.TCPServer;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    @FXML
    private ListView serverList;
    long pauseTime = 0;
    TCPServer server;

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
        //создание сервера
        new Thread(() -> {
            try {
                server = TCPServer.getInstance();
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки инициализации сервера
            }
        }).start();
//        Socket socket = null;
//        try {
//            socket = new Socket("127.0.0.1", 8080);
//            // Остальной код работы с сокетом
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            // Обработка ошибки подключения к хосту
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Обработка других вводно-выводных ошибок
//        }


//        Socket finalSocket = socket;
//        new Thread(() -> {
//            try {
//                // Бесконечный цикл для отправки запроса каждую секунду
//                while (true) {
//                    // Отправляем запрос на сервер
//                    PrintWriter out = new PrintWriter(finalSocket.getOutputStream(), true);
//                    out.println("GET_VALUE");
//
//                    // Получаем ответ от сервера
//                    BufferedReader in = new BufferedReader(new InputStreamReader(finalSocket.getInputStream()));
//                    String response;
//
//                    // Создаем список для хранения всех ответов
//                    List<String> responses = new ArrayList<>();
//
//                    while (!((response = in.readLine()).equals(""))) {
//                        responses.add(response);
//                    }
//
//                    // Очищаем и обновляем список на JavaFX потоке
//                    Platform.runLater(() -> {
//                        serverList.getItems().clear();
//                        serverList.getItems().addAll(responses);
//                    });
//
//                    // Ждем 1 секунду перед отправкой следующего запроса
//                    Thread.sleep(1000);
//                }
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

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




    private void saveConfig(){
        Properties saveProps = new Properties();
        saveProps.setProperty("probabilityTruck", String.valueOf(habitat.getTruckProbability()));
        saveProps.setProperty("probabilityPassenger", String.valueOf(habitat.getPassengerProbability())); // Записываем вероятность рождения

        saveProps.setProperty("truckTextField",String.valueOf(habitat.getN1()));
        saveProps.setProperty("passengerTextField",String.valueOf(habitat.getN2())); //Записываем переиод рождения

        saveProps.setProperty("lifeTimeTruck",String.valueOf(habitat.getLifeTimeN1()));
        saveProps.setProperty("lifeTimePassenger",String.valueOf(habitat.getLifeTimeN2())); //Записывем время жизни

        saveProps.setProperty("truckAiThread", truckAiThread.getValue());
        saveProps.setProperty("passengerAiThread", passengerAiThread.getValue());
        saveProps.setProperty("mainAiThread", mainAiThread.getValue());

        saveProps.setProperty("checkBoxMenu", String.valueOf(CheckBoxMenu.isSelected()));

        //запись кнопки
        saveProps.setProperty("truckOnButton", String.valueOf(truckOnButton.isDisable()));
        saveProps.setProperty("truckOffButton", String.valueOf(truckOffButton.isDisable()));
        saveProps.setProperty("passengerOnButton", String.valueOf(passengerOnButton.isDisable()));
        saveProps.setProperty("passengerOffButton", String.valueOf(passengerOffButton.isDisable()));

        //Состояние таймера
        saveProps.setProperty("open", String.valueOf(open.isSelected()));
        saveProps.setProperty("openDisabled", String.valueOf(open.isDisabled()));
        saveProps.setProperty("close", String.valueOf(close.isSelected()));
        saveProps.setProperty("closeDisabled", String.valueOf(close.isDisabled()));
        try {
            saveProps.storeToXML(new FileOutputStream("config.xml"), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void downloadConfig() {
        Properties loadProps = new Properties();
        try {
            loadProps.loadFromXML(new FileInputStream("config.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Вероятность рождения
        habitat.setTruckProbability(Float.parseFloat(loadProps.getProperty("probabilityTruck")));
        truckComboBox.setValue(String.valueOf((int) (habitat.getTruckProbability()*100)));
        habitat.setPassengerProbability(Float.parseFloat(loadProps.getProperty("probabilityPassenger")));
        passengerComboBox.setValue(String.valueOf((int) (habitat.getPassengerProbability()*100)));

        //Приоритеты потоков
        truckAI.setPriority(Integer.parseInt(loadProps.getProperty("truckAiThread")));
        truckAiThread.setValue(loadProps.getProperty("truckAiThread"));
        passengerAI.setPriority(Integer.parseInt(loadProps.getProperty("passengerAiThread")));
        passengerAiThread.setValue(loadProps.getProperty("passengerAiThread"));
        Thread.currentThread().setPriority(Integer.parseInt(loadProps.getProperty("mainAiThread")));
        mainAiThread.setValue(loadProps.getProperty("mainAiThread"));

        //Период рождения
        habitat.setTruckTime(Integer.parseInt(loadProps.getProperty("truckTextField")));
        truckTextField.setText(String.valueOf(loadProps.getProperty("truckTextField")));
        habitat.setPassengerTime(Integer.parseInt(loadProps.getProperty("passengerTextField")));
        passengerTextField.setText(String.valueOf(loadProps.getProperty("passengerTextField")));

        //Время жизни
        habitat.setLifeTimeN1(Integer.parseInt(loadProps.getProperty("lifeTimeTruck")));
        lifeTimeTruck.setText(loadProps.getProperty("lifeTimeTruck"));
        habitat.setLifeTimeN2(Integer.parseInt(loadProps.getProperty("lifeTimePassenger")));
        lifeTimePassenger.setText(loadProps.getProperty("lifeTimePassenger"));

        CheckBoxMenu.setSelected(Boolean.parseBoolean(loadProps.getProperty("checkBoxMenu")));
        CheckBoxMain.setSelected(Boolean.parseBoolean(loadProps.getProperty("checkBoxMenu")));

        //Кнопки AI
        truckOnButton.setDisable(Boolean.parseBoolean(loadProps.getProperty("truckOnButton")));
        truckOffButton.setDisable(Boolean.parseBoolean(loadProps.getProperty("truckOffButton")));
        passengerOnButton.setDisable(Boolean.parseBoolean(loadProps.getProperty("passengerOnButton")));
        passengerOffButton.setDisable(Boolean.parseBoolean(loadProps.getProperty("passengerOffButton")));

        //Радио батон
        open.setSelected(Boolean.parseBoolean(loadProps.getProperty("open")));
        open.setDisable(Boolean.parseBoolean(loadProps.getProperty("openDisabled")));
        close.setSelected(Boolean.parseBoolean(loadProps.getProperty("close")));
        close.setDisable(Boolean.parseBoolean(loadProps.getProperty("closeDisabled")));
        MenuRadioBtnShow.setSelected(Boolean.parseBoolean(loadProps.getProperty("open")));
        MenuRadioBtnShow.setDisable(Boolean.parseBoolean(loadProps.getProperty("openDisabled")));
        MenuRadioBtnHide.setSelected(Boolean.parseBoolean(loadProps.getProperty("close")));
        MenuRadioBtnHide.setDisable(Boolean.parseBoolean(loadProps.getProperty("closeDisabled")));

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