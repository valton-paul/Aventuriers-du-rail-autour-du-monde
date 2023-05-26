package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 * <p>
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    private final IJeu jeu;
    private final VuePlateau plateau;
    private VueAutresJoueurs autresJoueurs;
    private VueDemarragePartie initialisation;
    private VuePioche pioches;
    private HBox menusDuBas;
    private VBox choixEtInstruction;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        menusDuBas = new HBox();
        plateau = new VuePlateau();
        autresJoueurs = new VueAutresJoueurs(jeu);
        initialisation = new VueDemarragePartie();
        pioches = new VuePioche(jeu);
        choixEtInstruction = new VBox(); //Pour des boutons pions et les instructions



        this.setRight(autresJoueurs);
        this.setCenter(plateau);
        this.setBottom(menusDuBas);
        menusDuBas.setPadding(new Insets(10));
        menusDuBas.setAlignment(Pos.TOP_LEFT);
        menusDuBas.getChildren().add(initialisation);

    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        initialisation.createBindings();


        ((VueDuJeu) getScene().getRoot()).getJeu().jeuEnPreparationProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                menusDuBas.getChildren().remove(initialisation);
                menusDuBas.getChildren().add(pioches);
                pioches.createBindings();
                menusDuBas.setAlignment(Pos.CENTER_LEFT);
            }
        });
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
