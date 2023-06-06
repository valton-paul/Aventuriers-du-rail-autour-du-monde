package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.etatsJoueur.demandepions.PionsBateauDemandes;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Cette classe affiche les scores en fin de partie.
 * On peut éventuellement proposer de rejouer, et donc de revenir à la fenêtre principale
 */
public class VueResultats extends BorderPane {

    private RailsIHM ihm;
    private HBox bas;
    private HBox podium;
    private VBox stats;
    private Button rejouer;
    private Button quitter;
    private VBox deuxieme;
    private VBox premier;
    private VBox troisieme;
    Rectangle r2;
    Rectangle r1;
    Rectangle r3;

    public VueResultats(RailsIHM ihm) {
        this.ihm = ihm;
        bas = new HBox();
        podium = new HBox();
        stats = new VBox();


        String imagePath = "images/fond3.png";
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imagePath),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true)
        );
        this.setBackground(new Background(backgroundImage));


        this.setBottom(bas);
        this.setCenter(podium);
        this.setRight(stats);



        quitter = createJoliButton("Rejouer", Color.GREEN);
        rejouer = createJoliButton("Quitter", Color.RED);
        bas.getChildren().addAll(quitter, rejouer);

        bas.setSpacing(300);
        bas.setAlignment(Pos.CENTER);
        bas.setPadding(new Insets(50));

        BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN);
        Border border = new Border(borderStroke);
        bas.setBorder(border);

        // on crééé le podium

        deuxieme = new VBox();
        premier = new VBox();
        troisieme = new VBox();

        r2 = new Rectangle();
        r1 = new Rectangle();
        r3 = new Rectangle();

        r2.setHeight(450);
        r1.setHeight(540);
        r3.setHeight(375);
        r2.setWidth(300);
        r1.setWidth(300);
        r3.setWidth(300);
        r2.setFill(Color.SILVER);
        r1.setFill(Color.GOLD);
        r3.setFill(Color.CHOCOLATE);



        deuxieme.setAlignment(Pos.BOTTOM_CENTER);
        premier.setAlignment(Pos.BOTTOM_CENTER);
        troisieme.setAlignment(Pos.BOTTOM_CENTER);


        podium.getChildren().addAll(deuxieme, premier, troisieme);
        podium.setAlignment(Pos.CENTER);



    }

    public void creatBindings(){

        Image image1 = new Image("images/podium1.png");
        Image image2 = new Image("images/podium2.png");
        Image image3 = new Image("images/podium3.png");

        ImageView deuxiemeImage = new ImageView(image1);
        ImageView premierImage = new ImageView(image2);
        ImageView troisiemeImage = new ImageView(image3);

        deuxieme.getChildren().add(deuxiemeImage);
        premier.getChildren().add(premierImage);
        troisieme.getChildren().add(troisiemeImage);

        StackPane deuxiemeStack = new StackPane(r2, deuxiemeImage);
        StackPane premierStack = new StackPane(r1, premierImage);
        StackPane troisiemeStack = new StackPane(r3, troisiemeImage);

        deuxiemeImage.setLayoutX(r2.getWidth() / 2);
        premierImage.setLayoutX(r1.getWidth() / 2);
        troisiemeImage.setLayoutX(r3.getWidth() / 2);

        premier.getChildren().add(premierStack);
        deuxieme.getChildren().add(deuxiemeStack);
        troisieme.getChildren().add(troisiemeStack);



        SequentialTransition animation = new SequentialTransition();

        TranslateTransition troisiemeS = new TranslateTransition(Duration.seconds(1), troisieme);
        troisiemeS.setFromY(0-troisieme.getHeight()*2);
        troisiemeS.setToY(getScene().getHeight()-troisieme.getHeight()-this.bas.getHeight());

        TranslateTransition deuxiemeS = new TranslateTransition(Duration.seconds(1), deuxieme);
        deuxiemeS.setFromY(0-deuxieme.getHeight()*2);
        deuxiemeS.setToY(getScene().getHeight()-deuxieme.getHeight()-this.bas.getHeight());


        TranslateTransition premierS = new TranslateTransition(Duration.seconds(1), premier);
        premierS.setFromY(0-premier.getHeight()*2);
        premierS.setToY(getScene().getHeight()-premier.getHeight()-this.bas.getHeight());

        animation.getChildren().addAll(troisiemeS, deuxiemeS, premierS);
        animation.play();

        // classement pour podium
        PriorityQueue<IJoueur> classement = classment();

        ArrayList<IJoueur> tousLesjoueurs = new ArrayList<>();
        while(!classement.isEmpty()){
            tousLesjoueurs.add(classement.poll());
        }

        Label premierNom = new Label(tousLesjoueurs.get(0).getNom());
        premierNom.setFont(new Font("Arial", 30));
        premier.getChildren().add(0, premierNom);
        premierNom.setPadding(new Insets(0, 0, 20, 0));

        Label deuxièmeNom = new Label(tousLesjoueurs.get(1).getNom());
        deuxièmeNom.setFont(new Font("Arial", 30));
        deuxièmeNom.setPadding(new Insets(0, 0, 20, 0));
        deuxieme.getChildren().add(0, deuxièmeNom);

        if(tousLesjoueurs.size()>2){
            Label troisiemeNom = new Label(tousLesjoueurs.get(2).getNom());
            troisiemeNom.setFont(new Font("Arial", 30));
            troisiemeNom.setPadding(new Insets(0, 0, 20, 0));
            troisieme.getChildren().add(0, troisiemeNom);

        }


        // création du classement
        Label anonce = new Label("Classment");
        anonce.setMinWidth(stats.computeAreaInScreen());
        stats.getChildren().add(anonce);
        anonce.setStyle("-fx-font-size: 50px; -fx-border-width: 5px; -fx-border-color: black");
        stats.setStyle("-fx-border-width: 5px; -fx-border-color: black;");


        int compteur = 1;
        for(IJoueur joueurCourant : tousLesjoueurs){
            HBox numeroEtJoeur = new HBox();
            numeroEtJoeur.setMinHeight(stats.getHeight()/5);

            Label numero = new Label(""+compteur);
            numero.setStyle("-fx-font-size: 30px; -fx-border-width: 2px; -fx-border-color: black; -fx-padding: 10px");
            numero.setMinHeight(numeroEtJoeur.getHeight());

            VBox les2 = new VBox();
            Label nom = new Label(joueurCourant.getNom() + " | " + joueurCourant.getScore());
            nom.setStyle("-fx-font-size: 25px; -fx-padding: 15px");
            les2.getChildren().add(nom);

            Label port = new Label("Ports : " + joueurCourant.getNbPorts());
            Label nbBateaux  = new Label("Pions Bateau : " + joueurCourant.getNbPionsBateau());
            Label nbWagons = new Label("Pions Bateau : " + joueurCourant.getNbPionsWagon());


            port.setStyle("-fx-font-size: 10px;  -fx-padding: 3px");
            nbWagons.setStyle("-fx-font-size: 10px;  -fx-padding: 3px");
            nbBateaux.setStyle("-fx-font-size: 10px;  -fx-padding: 3px");
            les2.getChildren().addAll(port, nbWagons, nbBateaux);

            numeroEtJoeur.getChildren().addAll(numero, les2);

            stats.getChildren().add(numeroEtJoeur);

            numeroEtJoeur.setStyle("-fx-border-width: 2px; -fx-border-color: black;");

            compteur++;
        }


    }


    public PriorityQueue<IJoueur> classment() {

        PriorityQueue<IJoueur> classement = new PriorityQueue<>(new Comparator<IJoueur>() {
            @Override
            public int compare(IJoueur o1, IJoueur o2) {
                return  o2.getScore() - o1.getScore();
            }
        });
        classement.addAll(ihm.getJeu().getJoueurs());
        return classement;
    }

    private Button createJoliButton(String text, Color backgroundColor) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 40px; -fx-text-fill: white;");
        button.setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY)));
        button.setPadding(new Insets(10, 20, 10, 20));

        return button;
    }


}
