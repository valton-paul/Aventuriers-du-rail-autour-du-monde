package fr.umontpellier.iut.rails;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PodiumDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créer les marches du podium
        VBox podium = createPodium();

        // Créer la scène
        Scene scene = new Scene(podium, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Podium Demo");
        primaryStage.show();
    }

    private VBox createPodium() {
        VBox podium = new VBox();
        podium.setSpacing(10);
        podium.setPadding(new Insets(10));

        // Créer la première place
        Rectangle firstStep = createStep(200, 100, Color.GOLD);
        Label firstLabel = createLabel("Première place", Color.WHITE);
        StackPane firstPlace = new StackPane(firstStep, firstLabel);

        // Créer la deuxième place
        Rectangle secondStep = createStep(150, 75, Color.SILVER);
        Label secondLabel = createLabel("Deuxième place", Color.WHITE);
        StackPane secondPlace = new StackPane(secondStep, secondLabel);

        // Créer la troisième place
        Rectangle thirdStep = createStep(100, 50, Color.BROWN);
        Label thirdLabel = createLabel("Troisième place", Color.WHITE);
        StackPane thirdPlace = new StackPane(thirdStep, thirdLabel);

        // Placer les marches dans le podium
        podium.getChildren().addAll(secondPlace, thirdPlace, firstPlace);

        return podium;
    }

    private Rectangle createStep(double width, double height, Color color) {
        Rectangle step = new Rectangle(width, height);
        step.setFill(color);

        return step;
    }

    private Label createLabel(String text, Color color) {
        Label label = new Label(text);
        label.setTextFill(color);
        label.setStyle("-fx-font-size: 20px;");

        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
