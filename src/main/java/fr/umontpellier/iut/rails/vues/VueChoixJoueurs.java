package fr.umontpellier.iut.rails.vues;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 * <p>
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {


    private final ObservableList<String> nomsJoueurs;

    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }

    private HBox container;
    private ArrayList<ImageView> visages;
    private ArrayList<Integer> imagesDejaPrises;
    private Button ajouter;
    private BorderPane gandefentre;
    private boolean tousRempli;

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        container = new HBox();
        visages = new ArrayList<>();
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-BLEU.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-ROUGE.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-ROSE.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-JAUNE.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-VERT.png")));
        imagesDejaPrises = new ArrayList<>();
        ajouter = new Button("+");
        gandefentre = new BorderPane();
        tousRempli = true;


        String imageUrl = "images/fond23.png"; // ou fond22 j'hésite choisi celui que tu veux
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(imageUrl), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        gandefentre.setBackground(background);

        // on configure le bouton ajouter
        ajouter.setStyle("-fx-background-color: black; -fx-text-fill: white;-fx-font-size: 50px");
        ajouter.setPrefSize(100, 100);
        ajouter.setOnMouseEntered(event -> this.getScene().setCursor(Cursor.HAND));
        ajouter.setOnMouseExited(event -> this.getScene().setCursor(Cursor.DEFAULT));

        container.setMargin(ajouter, new Insets(50));


        // on configure le bouton lancer la partie
        Button lancer = new Button("> Lancer la partie <");
        //lancer.setPrefHeight(75);
        lancer.setFont(Font.font("Arial", 30));
        lancer.setTextFill(Color.WHITE);
        lancer.setBackground(new Background(new BackgroundFill(Color.web("#2ecc71"), new CornerRadii(5), Insets.EMPTY)));
        lancer.setPadding(new Insets(20, 50, 20, 50));
        lancer.setOnMouseEntered(event -> this.getScene().setCursor(Cursor.HAND));
        lancer.setOnMouseExited(event -> this.getScene().setCursor(Cursor.DEFAULT));


        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.25, Color.web("#2ecc71")),
                new Stop(0.5, Color.web("#13701D")),
                new Stop(0.75, Color.web("#2ecc71")));

        // Appliquer le dégradé initial sur le bouton
        lancer.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        lancer.setStyle("-fx-background-color: transparent;");

        // Créer l'animation de transition de couleur
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.seconds(5));
            }

            @Override
            protected void interpolate(double frac) {
                Color color = interpolateColor(gradient.getStops(), frac);
                lancer.setStyle("-fx-background-color: " + toRGBCode(color) + ";");
            }
        };

        animation.setCycleCount(Animation.INDEFINITE);
        animation.setAutoReverse(true);
        animation.play();


        gandefentre.setCenter(container);
        VBox bas = new VBox(lancer);
        bas.setAlignment(Pos.CENTER);
        bas.setPadding(new Insets(30));
        BorderStrokeStyle borderStyle = BorderStrokeStyle.SOLID;
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, borderStyle, null, new BorderWidths(5, 0, 0, 0));
        bas.setBorder(new Border(borderStroke));
        gandefentre.setBottom(bas);


        ArrayList<String> noms = new ArrayList<>();
        lancer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Node n : container.getChildren()) {
                    if (n instanceof VBox box) {
                        for (Node element : box.getChildren()) {
                            if (element instanceof TextField) {
                                if (!((TextField) element).getText().isEmpty()) {
                                    noms.add(((TextField) element).getText());
                                } else {
                                    tousRempli = false;
                                }
                            }

                        }
                    }
                }
                if (tousRempli) {
                    nomsJoueurs.addAll(noms);
                    close();
                }
            }
        });


        container.getChildren().add(ajouter);
        for (int i = 0; i < 2; i++) {
            creerJoueur();
        }
        ajouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (imagesDejaPrises.size() < 6) {
                    creerJoueur();
                }
                if (imagesDejaPrises.size() == 5) {
                    container.getChildren().remove(ajouter);
                }
            }
        });

        container.setAlignment(Pos.CENTER);
        Scene s = new Scene(gandefentre);
        this.setScene(s);
        this.setMaximized(true);
        this.setResizable(false);
        this.show();
    }


    public void creerJoueur() {
        VBox unJoueur = new VBox();
        String imageUrl = "images/fond3.png";
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(imageUrl), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        unJoueur.setBackground(background);
        BorderStrokeStyle borderStyle = BorderStrokeStyle.SOLID;
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, borderStyle, null, new BorderWidths(0, 5, 0, 5));
        unJoueur.setBorder(new Border(borderStroke));
        unJoueur.setPadding(new Insets(20));
        unJoueur.setSpacing(50);
        unJoueur.setMaxWidth(this.getWidth() / 6);


        // on créé l'ainmation pour les arrivée

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), unJoueur);
        scaleTransition.setFromX(0);
        scaleTransition.setToX(1);

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), unJoueur);
        translateTransition.setFromX(-unJoueur.getPrefWidth());
        translateTransition.setToX(0);

        scaleTransition.play();
        translateTransition.play();

        unJoueur.setAlignment(Pos.CENTER);
        Random chiffre = new Random();
        int randomNumber = chiffre.nextInt(5);
        while (imagesDejaPrises.contains(randomNumber)) {
            chiffre = new Random();
            randomNumber = chiffre.nextInt(5);
        }
        unJoueur.getChildren().add(visages.get(randomNumber));
        imagesDejaPrises.add(randomNumber);

        Label nom = new Label("Nom du joueur : ");


        nom.setFont(Font.font("Arial", 20));
        unJoueur.getChildren().add(nom);


        TextField t = new TextField();
        t.setPromptText("entrez un nom");
        t.setFont(Font.font("Arial", 25));
        t.setAlignment(Pos.CENTER);
        unJoueur.getChildren().add(t);

        Button enelever = new Button("Enlever");
        enelever.setFont(Font.font("Arial", 20));
        enelever.setTextFill(Color.WHITE);
        enelever.setPrefWidth(200);
        enelever.setBackground(new Background(new BackgroundFill(Color.web("#e74c3c"), new CornerRadii(5), Insets.EMPTY)));
        enelever.setPadding(new Insets(20));
        enelever.setOnMouseEntered(event -> this.getScene().setCursor(Cursor.HAND));
        enelever.setOnMouseExited(event -> this.getScene().setCursor(Cursor.DEFAULT));

        unJoueur.getChildren().add(enelever);

        container.getChildren().add(container.getChildren().size() - 1, unJoueur);
        int finalRandomNumber = randomNumber;
        enelever.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (container.getChildren().size() > 3) {
                    double durationSeconds = 0.5;
                    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(durationSeconds), unJoueur);
                    scaleTransition.setToX(0);
                    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(durationSeconds), unJoueur);
                    translateTransition.setToX(-unJoueur.getPrefWidth());
                    translateTransition.setOnFinished(event -> container.getChildren().remove(unJoueur));
                    scaleTransition.play();
                    translateTransition.play();
                    imagesDejaPrises.remove((Object) finalRandomNumber);
                }
                if (!container.getChildren().contains(ajouter)) {
                    container.getChildren().add(ajouter);
                }
            }
        });
    }

    private static Color interpolateColor(List<Stop> stops, double frac) {
        for (int i = 1; i < stops.size(); i++) {
            Stop start = stops.get(i - 1);
            Stop end = stops.get(i);
            if (frac >= start.getOffset() && frac <= end.getOffset()) {
                double t = (frac - start.getOffset()) / (end.getOffset() - start.getOffset());
                return interpolate(start.getColor(), end.getColor(), t);
            }
        }
        return stops.get(stops.size() - 1).getColor();
    }

    // Interpolation linéaire de deux couleurs avec un facteur donné
    private static Color interpolate(Color start, Color end, double t) {
        double r = (1 - t) * start.getRed() + t * end.getRed();
        double g = (1 - t) * start.getGreen() + t * end.getGreen();
        double b = (1 - t) * start.getBlue() + t * end.getBlue();
        double opacity = (1 - t) * start.getOpacity() + t * end.getOpacity();
        return new Color(r, g, b, opacity);
    }

    // Convertir une couleur en code RGB
    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }



    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }

    /**
     * Définit l'action à exécuter lorsque le nombre de participants change
     */
    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Integer> quandLeNombreDeJoueursChange) {
    }

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs(); i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            } else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        throw new RuntimeException("Methode à implémenter");
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     *
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        throw new RuntimeException("Methode à implémenter");
    }

}
