package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJeu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class VuePioche extends HBox {


    public VuePioche(IJeu jeu) {


        // on ajoute les 6 cartes de la pioches visibles
        VBox sixCartes = new VBox();
        HBox premiereLigne = new HBox();
        HBox deuxiemeLigne = new HBox();

        int compteur = 0;
        for (ICarteTransport c : jeu.cartesTransportVisiblesProperty()) {
            if (compteur % 2 == 0) {
                premiereLigne.getChildren().add(new VueCarteTransport(c, 1));
            } else {
                deuxiemeLigne.getChildren().add(new VueCarteTransport(c, 1));
            }
            compteur++;
        }
        sixCartes.getChildren().addAll(premiereLigne, deuxiemeLigne);
        this.getChildren().add(sixCartes);

        // on ajoute les pioches
        if (!jeu.piocheWagonVideProperty().getValue()) {
            Image wagon = new Image("images/cartesWagons/dos-WAGON.png");
            ImageView piocheWagon = new ImageView(wagon);
            this.getChildren().add(piocheWagon);
        }

        if (!jeu.piocheBateauVideProperty().getValue()) {
            Image bateau = new Image("images/cartesWagons/dos-BATEAU.png");
            ImageView piocheBateau = new ImageView(bateau);
            this.getChildren().add(piocheBateau);
        }
    }
}
