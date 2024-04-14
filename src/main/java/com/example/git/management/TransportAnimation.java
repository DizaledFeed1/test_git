package com.example.git.management;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class TransportAnimation {

    public static void moveTransport(ImageView imageView, int targetX, int targetY, double speed) {
        Path path = new Path();
        path.getElements().add(new MoveTo(imageView.getLayoutX(), imageView.getLayoutY()));
        path.getElements().add(new LineTo(targetX, targetY));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(speed)); // Устанавливаем длительность перемещения
        pathTransition.setPath(path);
        pathTransition.setNode(imageView);
        pathTransition.setInterpolator(Interpolator.LINEAR); // Линейная интерполяция для постоянной скорости

        pathTransition.play(); // Запускаем анимацию
    }


}

