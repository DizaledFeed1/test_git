package com.example.git.management;

import com.example.git.transports.Passenger;
import com.example.git.transports.Truck;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;

public class CurrentObject extends Application {
    private HashMap<Integer, Long> birthTimeMap;
    public void CurrentObject(HashMap<Integer, Long> birthTimeMap) {
         this.birthTimeMap = birthTimeMap;
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Текущие объекты");

        // Создаем кнопку для открытия модального окна
        Button openModalButton = new Button("Показать текущие объекты");
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

        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.TOP_CENTER); // Выравниваем содержимое по верхнему краю
        mainLayout.setPadding(new Insets(50));

        Scene modalScene = new Scene(mainLayout, 300, 200);
        modalStage.setScene(modalScene);
        modalStage.showAndWait(); // Ждем, пока окно будет закрыто
    }

    public static void main(String[] args) {
        launch(args);
    }
}
