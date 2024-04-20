package com.example.git.management;

import com.example.git.transports.Passenger;
import com.example.git.transports.Truck;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


public class Statistic extends Application {

    private HelloController helloController;
    public Statistic(HelloController helloController) {
        this.helloController = helloController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Статистика");

        // Создаем кнопку для открытия модального окна
        Button openModalButton = new Button("Показать статистику");
        openModalButton.setOnAction(e -> openModalWindow(10));

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER); // Центрируем содержимое по центру
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(openModalButton);

        Scene scene = new Scene(layout, 250, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void openModalWindow(long startTime) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Модальное окно со статистикой");

        // Здесь можно добавить содержимое модального окна, например:
        String text = "Легковых автомобилей:" + Passenger.intPassenger + "\nГрузовыx автомобилей:" + Truck.intTruck + "\nВремя: "+ ((startTime - helloController.initializationTime - helloController.pauseTime)/1000);
        TextArea textArea = new TextArea(text);
        textArea.setStyle("-fx-control-inner-background: rgba(154,7,167);-fx-text-fill:black");
        textArea.setEditable(false);
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");

        // Установка размеров кнопок
        okButton.setPrefSize(70, 30);
        okButton.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0,0.2),CornerRadii.EMPTY, Insets.EMPTY)));
        okButton.setTextFill(Color.BLACK);
        cancelButton.setPrefSize(70, 30);
        cancelButton.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0,0.2),CornerRadii.EMPTY, Insets.EMPTY)));
        cancelButton.setTextFill(Color.BLACK);

        okButton.setOnAction(e -> {
            modalStage.close();
        });
        cancelButton.setOnAction(e -> {
            modalStage.close();
            helloController.start();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(okButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER); // Выравниваем кнопки по центру

        VBox mainLayout = new VBox(10);
        mainLayout.setBackground(new Background(new BackgroundFill(Color.rgb(154,7,167),CornerRadii.EMPTY, Insets.EMPTY)));
        mainLayout.setAlignment(Pos.TOP_CENTER); // Выравниваем содержимое по верхнему краю
        mainLayout.setPadding(new Insets(50));
        mainLayout.getChildren().addAll(textArea, buttonBox);

        Scene modalScene = new Scene(mainLayout, 300, 200);
        modalStage.setScene(modalScene);
        modalStage.showAndWait(); // Ждем, пока окно будет закрыто
    }
    public void openModalWindowLife(HashMap<Integer, Long> birthTimeMap) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Модальное окно со статистикой на данный момент");

        ObservableList<String> items = FXCollections.observableArrayList();
        items.clear();
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Integer, Long> entry : birthTimeMap.entrySet()) {
            // Учитываем время начала паузы при расчете времени жизни объектов
            long birthTime = entry.getValue();
            items.add("ID: " + entry.getKey() + ", Время рождения: " + birthTime);
        }

        ListView<String> listView = new ListView<>(items);
        VBox root = new VBox(listView);
        Scene scene = new Scene(root, 300, 250);

        modalStage.setScene(scene);
        modalStage.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
