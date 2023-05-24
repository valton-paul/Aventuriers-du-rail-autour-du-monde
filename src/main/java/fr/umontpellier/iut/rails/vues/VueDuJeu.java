package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    private final IJeu jeu;
    private final VuePlateau plateau;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        HBox menusDuBas = new HBox();
        plateau = new VuePlateau();
        VueAutresJoueurs autresJoueurs = new VueAutresJoueurs(jeu);
        VueDemarragePartie lesPioches = new VueDemarragePartie(jeu);

        this.setRight(autresJoueurs);
        this.setCenter(plateau);
        this.setBottom(menusDuBas);

        menusDuBas.setPadding(new Insets(10));
        menusDuBas.setAlignment(Pos.TOP_LEFT);
        menusDuBas.getChildren().add(lesPioches);
        menusDuBas.getChildren().add(new VuePioche(jeu));
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
