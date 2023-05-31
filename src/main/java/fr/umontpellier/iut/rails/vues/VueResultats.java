package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.etatsJoueur.demandepions.PionsBateauDemandes;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

    public VueResultats(RailsIHM ihm) {
        this.ihm = ihm;
        bas = new HBox();
        podium = new HBox();
        stats = new VBox();

        this.setBottom(bas);
        this.setCenter(podium);
        this.setRight(stats);


        // on configure le bas
        rejouer = new Button("Rejouer");
        quitter = new Button("Quitter");
        bas.getChildren().addAll(rejouer, quitter);


        // on crééé le podium

        deuxieme = new VBox();
        premier = new VBox();
        troisieme = new VBox();

        Rectangle r2 = new Rectangle();
        Rectangle r1 = new Rectangle();
        Rectangle r3 = new Rectangle();

        r2.setHeight(150);
        r1.setHeight(180);
        r3.setHeight(125);
        r2.setWidth(100);
        r1.setWidth(100);
        r3.setWidth(100);
        r2.setFill(Color.SILVER);
        r1.setFill(Color.GOLD);
        r3.setFill(Color.CHOCOLATE);


        deuxieme.getChildren().add(r2);
        premier.getChildren().add(r1);
        troisieme.getChildren().add(r3);

        deuxieme.setAlignment(Pos.BOTTOM_CENTER);
        premier.setAlignment(Pos.BOTTOM_CENTER);
        troisieme.setAlignment(Pos.BOTTOM_CENTER);


        podium.getChildren().addAll(deuxieme, premier, troisieme);

        podium.setAlignment(Pos.BOTTOM_CENTER);





    }

    public void creatBindings(){
        // classement pour podium
        PriorityQueue<IJoueur> classement = classment();

        Label premierNom = new Label(classement.poll().getNom());
        premierNom.setFont(new Font("Arial", 30));
        premier.getChildren().add(0, premierNom);
        premierNom.setPadding(new Insets(0, 0, 20, 0));

        Label deuxièmeNom = new Label(classement.poll().getNom());
        deuxièmeNom.setFont(new Font("Arial", 30));
        deuxièmeNom.setPadding(new Insets(0, 0, 20, 0));
        deuxieme.getChildren().add(0, deuxièmeNom);

        if(classement.peek() != null){
            Label troisiemeNom = new Label(classement.poll().getNom());
            troisiemeNom.setFont(new Font("Arial", 30));
            troisiemeNom.setPadding(new Insets(0, 0, 20, 0));
            troisieme.getChildren().add(0, troisiemeNom);

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


}
