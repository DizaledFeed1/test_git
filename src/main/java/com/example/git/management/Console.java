package com.example.git.management;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.security.Key;
import java.util.Locale;
import java.util.Objects;

public class Console {
    private HelloController helloController;
    @FXML
    private TextArea textArea;

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }
    public void enterConsole (KeyEvent keyEvent){
        textArea.getScene().setOnKeyReleased((KeyEvent event) ->{
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER)
            {
                handleCommand(textArea.getText());
            }
        });
    }
    private void handleCommand(String command){
        String[] parts = command.split("\\s+");
        String commandName = parts[0].toLowerCase(Locale.ROOT);
        switch (commandName){
            case "help" -> {
                printConsole("\"stopAI\" - остановить расчёт движения объектов \n" +
                        "\"resumAI\" - продолжить расчёт движения объектов");
            }
            case "stopai" ->{
                stopAI();
            }
            case"resumai" ->{
                resumAI();
            }
            default -> {
                printConsole("введите \"help\" - для отображения всех доступных команд");
            }
        }
        System.out.println("Введено: " + commandName);
    }
    private void resumAI(){
        helloController.swapAI(2);
        helloController.getTruckAI().resumeAI();
        helloController.getPassengerAI().resumeAI();
    }
    private void stopAI(){
        helloController.swapAI(1);
        helloController.getTruckAI().pause();
        helloController.getPassengerAI().pause();
    }
    private void printConsole(String text){
        textArea.setText(text + "\n");
    }

}
