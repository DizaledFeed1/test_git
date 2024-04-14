package com.example.git.management;

import com.example.git.transports.Transport;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.util.Duration;

public class PassengerAI extends BaseAI {
    private final Transport transport;

    public PassengerAI(Transport transport, int interval) {
        super(interval);
        this.transport = transport;
    }

    @Override
    protected void moveTransport() {
        Platform.runLater(() -> {
            // Создаем анимацию перемещения
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(5), transport.getImageView());

            double deltaX = transport.getFinalX() - transport.getImageView().getLayoutX();
            double deltaY = transport.getFinalY() - transport.getImageView().getLayoutY();

            // Устанавливаем конечные координаты
            translateTransition.setByX(deltaX);
            translateTransition.setByY(deltaY);

            translateTransition.setOnFinished(event -> {
                // Действия после завершения анимации, например, завершение потока AI
                stop();
            });

            // Запускаем анимацию
            translateTransition.play();
        });
    }
}
