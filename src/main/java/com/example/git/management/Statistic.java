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
        openModalButton.setOnAction(e -> openModalWindow());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER); // Центрируем содержимое по центру
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(openModalButton);

        Scene scene = new Scene(layout, 250, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Метод для открытия модального окна
    public void openModalWindow() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Модальное окно со статистикой");

        // Здесь можно добавить содержимое модального окна, например:
        String text = "Легковых автомобилей:" + Passenger.intPassenger + "\nГрузовыx автомобилей:" + Truck.intTruck + "\nВремя: "+ ((System.currentTimeMillis() - helloController.initializationTime)/1000);
        TextArea textArea = new TextArea(text);
        textArea.setEditable(false);
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");

        // Установка размеров кнопок
        okButton.setPrefSize(70, 30);
        cancelButton.setPrefSize(70, 30);

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
        mainLayout.setAlignment(Pos.TOP_CENTER); // Выравниваем содержимое по верхнему краю
        mainLayout.setPadding(new Insets(50));
        mainLayout.getChildren().addAll(textArea, buttonBox);

        Scene modalScene = new Scene(mainLayout, 300, 200);
        modalStage.setScene(modalScene);
        modalStage.showAndWait(); // Ждем, пока окно будет закрыто
    }

    public static void main(String[] args) {
        launch(args);
    }
}
