package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.data.Ville;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    public VueJoueurCourant(IJeu jeu) {

        VBox principal = new VBox();
        HBox profil = new HBox();
        principal.getChildren().add(profil);
        this.getChildren().add(principal);
        Label label = new Label(jeu.joueurCourantProperty().get().getNom() + " - " + jeu.joueurCourantProperty().get().getScore() + " points");
        label.setTextFill(Color.web(getCouleur(jeu.joueurCourantProperty().get())));
        label.setFont(new Font("Arial", 30));
        profil.getChildren().add(label);
        this.setSpacing(20);
        this.setPadding(new Insets(0, 10, 0, 5));

        // on affiche les pions du joueur (+ le nb de ports)
        VBox pions = new VBox();
        Label nbPionsWagon = new Label("Pions Wagons : " + jeu.joueurCourantProperty().get().getNbPionsWagon());
        Label nbPionsBateau = new Label("Pions Bateaux : " + jeu.joueurCourantProperty().get().getNbPionsBateau());
        Label nbPorts = new Label("Ports : " + jeu.joueurCourantProperty().get().getNbPorts());
        pions.getChildren().addAll(nbPionsWagon, nbPionsBateau, nbPorts);
        principal.getChildren().add(pions);

        // on affiche les destinations
        VBox destinations = new VBox();
        int compteur = 0;
        for (IDestination d : jeu.joueurCourantProperty().get().getDestinations()){
            String villesDest = "D" + compteur;
            for(String v : d.getVilles()){
                villesDest += v + " - ";
            }
            Label uneDestination = new Label(villesDest);
            destinations.getChildren().add(uneDestination);
            compteur++;
        }
        principal.getChildren().add(destinations);

        // on affiche les cartes Wagons
        HBox wagons = new HBox();
        HBox bateaux = new HBox();
        for(ICarteTransport carte : jeu.joueurCourantProperty().get().getCartesTransport()){
            if(carte.estBateau()){
                continue;
            }
        }

    }

    public String getCouleur(IJoueur j) {
        String color = "";
        switch (j.getCouleur()) {
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
