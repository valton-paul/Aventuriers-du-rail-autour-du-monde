package fr.umontpellier.iut.rails;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Confetti extends Application {

    private static final int NUM_CONFETTI = 1000;

    @Override
    public void start(Stage primaryStage) {
        HBox container = new HBox();
        container.setPrefSize(800, 600);

        Group root = new Group(container);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Confetti Falling");
        primaryStage.setScene(scene);
        primaryStage.show();

        //dropConfetti(container);
    }

    public static void dropConfetti(HBox container) {
        Random random = new Random();

        for (int i = 0; i < NUM_CONFETTI; i++) {
            Rectangle confetti = new Rectangle(10, 10, getRandomColor());

            confetti.setTranslateX(random.nextDouble() * container.getWidth());
            confetti.setTranslateY(-20);

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(5), confetti);
            translateTransition.setToY(container.getHeight() + 20);
            translateTransition.setInterpolator(Interpolator.LINEAR);

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), confetti);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setDelay(Duration.seconds(4));

            ParallelTransition parallelTransition = new ParallelTransition(confetti, translateTransition, fadeTransition);
            parallelTransition.setDelay(Duration.seconds(random.nextDouble() * 3));
            parallelTransition.play();

            container.getChildren().add(confetti);
        }
    }

    private static Color getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
