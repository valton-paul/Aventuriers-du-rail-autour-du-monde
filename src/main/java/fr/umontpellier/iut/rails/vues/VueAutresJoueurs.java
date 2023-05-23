package fr.umontpellier.iut.rails.vues;


import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {
    private ArrayList<IJoueur> listeJoueurOrdre;

    public VueAutresJoueurs(IJeu jeu)
    {
        listeJoueurOrdre = new ArrayList<>(jeu.getJoueurs());
        creationVBoxJoueurSaufCourant();
        this.setSpacing(20);
        //this.setPadding(new Insets(0, 10, 0, 5));

        ChangeListener<IJoueur> joueurCourant = new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur iJoueur, IJoueur t1) {
                getChildren().clear();
                listeJoueurOrdre.remove(t1);
                creationVBoxJoueurSaufCourant();
                listeJoueurOrdre.add(t1);
                VueJoueurCourant vjc = new VueJoueurCourant(jeu, t1);
                Color backgroundColor = Color.web(getCouleur(t1));
                Color colorWithOpacity = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 0.5);
                BackgroundFill backgroundFill = new BackgroundFill(colorWithOpacity, null, null);
                Background background = new Background(backgroundFill);
                vjc.setBackground(background);
                getChildren().add(0, vjc);
            }
        };
        jeu.joueurCourantProperty().addListener(joueurCourant);
    }

    public void creationVBoxJoueurSaufCourant()
    {
        for (IJoueur j : listeJoueurOrdre)
        {
            VBox principal = new VBox();
            HBox profil = new HBox();
            principal.getChildren().add(profil);
            this.getChildren().add(principal);
            Label label = new Label(j.getNom() + " - " + j.getScore() + " points");
            Label pionsWagon = new Label("Pions wagons : "+j.getNbPionsWagon());
            Label pionsBateau = new Label("Pions bateaux : "+j.getNbPionsBateau());
            label.setTextFill(Color.web(getCouleur(j)));
            principal.getChildren().addAll(pionsWagon, pionsBateau);
            profil.getChildren().add(label);
            label.setFont(new Font("Arial", 20));
        }
    }

    public static String getCouleur(IJoueur j)
    {
        String color = "";
        switch (j.getCouleur())
        {
            case BLEU -> color = "BLUE";
            case ROSE -> color = "DEEPPINK";
            case VERT -> color = "GREEN";
            case JAUNE -> color = "DARKGOLDENROD";
            case ROUGE -> color = "RED";
            case ARGENT -> color = "SILVER";
        }
        return color;
    }
}
