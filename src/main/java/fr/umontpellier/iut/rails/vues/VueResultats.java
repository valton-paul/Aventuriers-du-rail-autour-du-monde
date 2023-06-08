package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.Confetti;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.etatsJoueur.demandepions.PionsBateauDemandes;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
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
    private VBox stats1;
    private VBox stats2;
    private ScrollPane tout;
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
        stats2 = new VBox();
        stats1 = new VBox();
        tout = new ScrollPane();


        this.setStyle("-fx-background-color: #383535;");


        this.setBottom(bas);
        this.setCenter(podium);
        this.setRight(stats1);



        quitter = createJoliButton("Rejouer", Color.GREEN);
        quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) quitter.getScene().getWindow(); // Obtient la référence à la fenêtre courante
                stage.close(); // Ferme la fenêtre courante
                ihm.debuterJeu();
            }
        });
        rejouer = createJoliButton("Quitter", Color.RED);
        rejouer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ihm.arreterJeu();
            }
        });
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

        r2.setHeight(375);
        r1.setHeight(450);
        r3.setHeight(300);
        r2.setWidth(300);
        r1.setWidth(300);
        r3.setWidth(300);
        r2.setFill(Color.SILVER);
        r1.setFill(Color.GOLD);
        r3.setFill(Color.CHOCOLATE);



        deuxieme.setAlignment(Pos.BOTTOM_CENTER);
        premier.setAlignment(Pos.BOTTOM_CENTER);
        troisieme.setAlignment(Pos.BOTTOM_CENTER);


        Image image1 = new Image("images/podium1.png");
        Image image2 = new Image("images/podium2.png");
        Image image3 = new Image("images/podium3.png");

        ImageView deuxiemeImage = new ImageView(image1);
        ImageView premierImage = new ImageView(image2);
        ImageView troisiemeImage = new ImageView(image3);


        StackPane deuxiemeStack = new StackPane(r2, deuxiemeImage);
        StackPane premierStack = new StackPane(r1, premierImage);
        StackPane troisiemeStack = new StackPane(r3, troisiemeImage);


        premier.getChildren().add(premierStack);
        deuxieme.getChildren().add(deuxiemeStack);
        troisieme.getChildren().add(troisiemeStack);


        podium.getChildren().addAll(deuxieme, premier, troisieme);
        podium.setAlignment(Pos.CENTER);
    }

    public void creatBindings(){

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


        int space = 10;
        int taillle = 275;
        int paddingTexte = 10;

        Image premierImage = new Image("images/cartesWagons/avatar-"+tousLesjoueurs.get(0).getCouleur().toString().toUpperCase()+".png");
        ImageView img1 = new ImageView(premierImage);
        img1.setFitHeight(taillle);
        img1.setPreserveRatio(true);
        Label premierNom = new Label("Score : " + tousLesjoueurs.get(0).getScore() + "");
        premierNom.setFont(new Font("Arial", 30));
        premierNom.setPadding(new Insets(paddingTexte));
        premierNom.setTextFill(Color.WHITE);
        premier.getChildren().add(0, img1);
        premier.getChildren().add(1, premierNom);
        premier.setSpacing(space);

        Image deuxiemeImage = new Image("images/cartesWagons/avatar-"+tousLesjoueurs.get(1).getCouleur().toString().toUpperCase()+".png");
        ImageView img2 = new ImageView(deuxiemeImage);
        img2.setFitHeight(taillle);
        img2.setPreserveRatio(true);
        Label deuxiemeNom = new Label("Score : " + tousLesjoueurs.get(1).getScore() + "");
        deuxiemeNom.setFont(new Font("Arial", 30));
        deuxiemeNom.setPadding(new Insets(paddingTexte));
        deuxiemeNom.setTextFill(Color.WHITE);
        deuxieme.getChildren().add(0, img2);
        deuxieme.getChildren().add(1, deuxiemeNom);
        deuxieme.setSpacing(space);


        if(tousLesjoueurs.size()>2){
            Image troisiemeImage = new Image("images/cartesWagons/avatar-"+tousLesjoueurs.get(2).getCouleur().toString().toUpperCase()+".png");
            ImageView img3 = new ImageView(troisiemeImage);
            img3.setFitHeight(taillle);
            img3.setPreserveRatio(true);
            Label troisiemeNom = new Label("Score : " + tousLesjoueurs.get(2).getScore() + "");
            troisiemeNom.setFont(new Font("Arial", 30));
            troisiemeNom.setPadding(new Insets(paddingTexte));
            troisiemeNom.setTextFill(Color.WHITE);
            troisieme.getChildren().add(0, img3);
            troisieme.getChildren().add(1, troisiemeNom);
            troisieme.setSpacing(space);
        }


        // création du classement
        Label anonce = new Label("Classment");
        stats1.getChildren().add(anonce);
        stats1.setStyle("-fx-border-width: 2px; -fx-border-color: black;  -fx-background-color: WHITE");

        anonce.setStyle("-fx-background-color: WHITE");
        anonce.setStyle("-fx-font-size: 50px; -fx-border-width: 0px 0px 2px 0px; -fx-border-color: black; -fx-padding: 30px;");
        anonce.setMinWidth(stats1.computeAreaInScreen());


        ScrollPane tout = new ScrollPane();
        tout.setContent(stats2);

        stats1.getChildren().add(tout);

        int compteur = 1;
        for(IJoueur joueurCourant : tousLesjoueurs){
            HBox numeroEtJoeur = new HBox();

            VBox num = new VBox();
            num.setAlignment(Pos.CENTER);
            num.setStyle("-fx-font-size: 30px; -fx-border-width: 0px 2px 0px 0px; -fx-border-color: black; -fx-padding: 30px 15px; -fx-background-color: WHITE");
            Label numero = new Label(""+compteur);
            num.setMinHeight(numeroEtJoeur.getMinHeight());
            num.getChildren().add(numero);

            VBox les2 = new VBox();

            HBox visageEtNom = new HBox();
            Image troisiemeImage = new Image("images/cartesWagons/avatar-"+joueurCourant.getCouleur().toString().toUpperCase()+".png");
            ImageView img = new ImageView(troisiemeImage);
            img.setFitHeight(75);
            img.setPreserveRatio(true);
            Label nom = new Label(joueurCourant.getNom() + " | " + joueurCourant.getScore());
            nom.setStyle("-fx-font-size: 30px; -fx-padding: 30px 0px 30px 15px");
            visageEtNom.getChildren().addAll(img, nom);
            les2.getChildren().add(visageEtNom);

            Label port = new Label("Ports : " + joueurCourant.getNbPorts());
            Label nbBateaux  = new Label("Pions Bateau : " + joueurCourant.getNbPionsBateau());
            Label nbWagons = new Label("Pions Bateau : " + joueurCourant.getNbPionsWagon());
            Label nbRoute = new Label("Nombre de ports : " + joueurCourant.getNbPorts());


            port.setStyle("-fx-font-size: 15px;  -fx-padding: 5px");
            nbWagons.setStyle("-fx-font-size: 15px;  -fx-padding: 5px");
            nbBateaux.setStyle("-fx-font-size: 15px;  -fx-padding: 5px");
            les2.getChildren().addAll(port, nbWagons, nbBateaux);

            numeroEtJoeur.getChildren().addAll(num, les2);
            numeroEtJoeur.setStyle("-fx-border-width: 0px 0px 2px 0px; -fx-border-color: black;");




            stats2.getChildren().add(numeroEtJoeur);
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
        button.setPadding(new Insets(15, 200, 15, 200));

        return button;
    }

}
