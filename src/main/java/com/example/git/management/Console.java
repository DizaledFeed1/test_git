package com.example.git.management;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String command = textArea.getText().trim(); // Получаем весь введенный текст и удаляем пробелы по краям
            int lastNewLineIndex = command.lastIndexOf("\n"); // Находим индекс последнего символа новой строки
            String lastLine = command.substring(lastNewLineIndex + 1); // Выделяем слово после последнего символа новой строки
            handleCommand(lastLine); // Передаем слово на обработку команды
        }
    }
    private void handleCommand(String command){
        textArea.setStyle("-fx-text-fill:yellow;-fx-control-inner-background: rgba(88,88,88);");
        command = command.trim();

        // Находим индекс последнего пробела в строке
        int lastSpaceIndex = command.lastIndexOf(" ");

        // Если в строке нет пробелов, последнее слово - это вся строка
        String lastWord = (lastSpaceIndex == -1) ? command : command.substring(lastSpaceIndex + 1);

        String commandName = lastWord.toLowerCase(Locale.ROOT);
        switch (commandName){
            case "help" -> {
                printConsole("\"stopAI\" - остановить расчёт движения объектов \n" +
                        "\"resumAI\" - продолжить расчёт движения объектов");
            }
            case "stopai" ->{
                stopAI();
                printConsole("работа потоков приостановлена");
            }
            case"resumai" ->{
                resumAI();
                printConsole("работа потоков возобновелна");
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
        textArea.appendText(text + "\n");
    }

}
