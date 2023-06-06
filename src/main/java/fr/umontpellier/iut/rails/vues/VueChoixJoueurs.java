package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import javafx.animation.*;
import javafx.application.Platform;
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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 * <p>
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private static HashMap<String, String> imagesDesJoueurs;


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
        for (ImageView imageView : visages) {
            String imagePath = imageView.getImage().getUrl();
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageID = imageName.substring(0, imageName.lastIndexOf("."));
            imageView.setId(imageID);
            imageView.setOnMouseEntered(event -> this.getScene().setCursor(Cursor.HAND));
            imageView.setOnMouseExited(event -> this.getScene().setCursor(Cursor.DEFAULT));
            imageView.setOnMouseClicked(e -> afficherChoix(imageView));
        }

        imagesDejaPrises = new ArrayList<>();
        ajouter = new Button("+");
        gandefentre = new BorderPane();
        tousRempli = true;
        imagesDesJoueurs = new HashMap<>();


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
                new Stop(0.5, Color.web("#1d8348")),
                new Stop(0.75, Color.web("#2ecc71")));

        // Appliquer le dégradé initial sur le bouton
        lancer.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        lancer.setStyle("-fx-background-color: transparent;");

        // Créer l'animation de transition de couleur avec une durée de 1 seconde
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.seconds(3));
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
                ArrayList<String> nomsDejaPris = new ArrayList<>();
                tousRempli = true;
                for (Node n : container.getChildren()) {
                    if (n instanceof VBox box) {
                        for (Node element : box.getChildren()) {
                            if (element instanceof TextField) {
                                if (!((TextField) element).getText().isEmpty()) {
                                    String nomDuJoueur = ((TextField) element).getText();
                                    if(nomsDejaPris.contains(nomDuJoueur)){
                                        tousRempli = false;
                                    }else{
                                        noms.add(nomDuJoueur);
                                        nomsDejaPris.add(nomDuJoueur);
                                    }
                                } else {
                                    tousRempli = false;
                                }
                            }

                        }
                    }
                }
                if (tousRempli) {
                    for (Node n : container.getChildren()) {
                        if (n instanceof VBox box) {
                            String nomJoueur = "";
                            String lienImage = "";
                            for (Node element : box.getChildren()) {
                                if (element instanceof TextField) {
                                    nomJoueur = ((TextField) element).getText();
                                } else if (element instanceof ImageView) {
                                    lienImage = element.getId();
                                }
                            }
                            imagesDesJoueurs.put(nomJoueur, lienImage);
                        }
                    }
                    VueAutresJoueurs.setImages(imagesDesJoueurs);

                    nomsJoueurs.addAll(noms);
                    close();
                } else {
                    vibrate(VueChoixJoueurs.this);
                    noms.clear();
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
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);

        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static void vibrate(Stage stage) {
        double durationMillis = 100; // Durée de la vibration en millisecondes
        double amplitude = 10; // Amplitude de la vibration en pixels

        // Animation de translation
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(stage.getScene().getRoot().translateXProperty(), -amplitude)),
                new KeyFrame(Duration.millis(durationMillis / 4), new KeyValue(stage.getScene().getRoot().translateXProperty(), amplitude)),
                new KeyFrame(Duration.millis(durationMillis / 2), new KeyValue(stage.getScene().getRoot().translateXProperty(), -amplitude)),
                new KeyFrame(Duration.millis(durationMillis * 3 / 4), new KeyValue(stage.getScene().getRoot().translateXProperty(), amplitude)),
                new KeyFrame(Duration.millis(durationMillis), new KeyValue(stage.getScene().getRoot().translateXProperty(), 0))
        );
        timeline.setCycleCount((int) (durationMillis / (2 * amplitude)));

        // Filtre rouge semi-transparent
        Rectangle filter = new Rectangle(stage.getWidth(), stage.getHeight(), Color.rgb(255, 0, 0, 0.5));

        // Lancer l'animation
        timeline.setOnFinished(event -> {
            Platform.runLater(() -> {
                Pane root = (Pane) stage.getScene().getRoot();
                root.getChildren().remove(filter);
            });
        });

        Pane root = (Pane) stage.getScene().getRoot();
        root.getChildren().add(filter);
        timeline.play();
    }

    private void afficherChoix(ImageView imgCourante)
    {
        if(imagesDejaPrises.size() == 5) return;
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(this);
        HBox imageDispo = new HBox();
        imageDispo.setSpacing(25);

        for(int i = 0; i < visages.size(); i++)
        {
            if(!imagesDejaPrises.contains(i))
            {
                ImageView otherImg = new ImageView((visages.get(i).getImage()));
                otherImg.setOnMouseEntered(event -> this.getScene().setCursor(Cursor.HAND));
                otherImg.setOnMouseExited(event -> this.getScene().setCursor(Cursor.DEFAULT));
                otherImg.setId(""+i);
                imageDispo.getChildren().add(otherImg);
                otherImg.setOnMouseClicked(e -> {
                    ArrayList<Node> removedList = new ArrayList<>();
                    imagesDejaPrises.add(Integer.parseInt(otherImg.getId()));
                    imagesDejaPrises.remove((Object)visages.indexOf(imgCourante));
                    boolean oui = false;
                    for(Node node : this.container.getChildren())
                    {
                        if(node instanceof VBox n)
                        {
                            for(Node nodeFils : n.getChildren())
                            {
                                if(nodeFils instanceof ImageView img)
                                {
                                    if(img.equals(imgCourante))
                                    {
                                        removedList.add(nodeFils);
                                        oui = true;
                                    }
                                }
                                if(oui) break;
                            }
                            if(oui)
                            {
                                n.getChildren().removeAll(removedList);
                                for(ImageView k : visages)
                                {
                                    if(k.getImage().equals(otherImg.getImage()))
                                    {
                                        n.getChildren().add(0, k);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    popup.close();
                });
            }
        }
        popup.setScene(new Scene(imageDispo));
        popup.showAndWait();
    }
}
