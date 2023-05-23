package fr.umontpellier.iut.rails.vues;


import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {

    public VueAutresJoueurs(IJeu jeu)
    {
        for (IJoueur j : jeu.getJoueurs())
        {
            if(j.equals(jeu.joueurCourantProperty().get()))
            {
                this.getChildren().add(new VueJoueurCourant());
            }
            else
            {
                VBox principal = new VBox();
                HBox profil = new HBox();
                principal.getChildren().add(profil);
                this.getChildren().add(principal);
                Label label = new Label(j.getNom() + " - " + j.getScore() + " points");
                label.setTextFill(Color.web(getCouleur(j)));
                profil.getChildren().add(label);
                label.setFont(new Font("Arial", 20));
            }
        }
        this.setSpacing(20);
        this.setPadding(new Insets(0, 10, 0, 5));
    }

    public String getCouleur(IJoueur j)
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
