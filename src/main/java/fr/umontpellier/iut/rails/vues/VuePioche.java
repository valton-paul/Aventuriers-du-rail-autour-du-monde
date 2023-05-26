package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJeu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class VuePioche extends HBox {
    private VBox sixCartes;
    private HBox premiereLigne;
    private HBox deuxiemeLigne;
    private Label instructions;
    private VBox leftInfo;

    public VuePioche(IJeu jeu) {


        // on ajoute les 6 cartes de la pioches visibles
        sixCartes = new VBox();
        premiereLigne = new HBox();
        deuxiemeLigne = new HBox();
        instructions = new Label();
        leftInfo = new VBox();
        leftInfo.getChildren().add(instructions);

        this.getChildren().add(leftInfo);
        // on ajoute les pioches
        if (!jeu.piocheWagonVideProperty().getValue()) {
            Image wagon = new Image("images/cartesWagons/dos-WAGON.png");
            ImageView piocheWagon = new ImageView(wagon);
            piocheWagon.setFitHeight(175);
            piocheWagon.setPreserveRatio(true);
            this.getChildren().add(piocheWagon);
        }

        if (!jeu.piocheBateauVideProperty().getValue()) {
            Image bateau = new Image("images/cartesWagons/dos-BATEAU.png");
            ImageView piocheBateau = new ImageView(bateau);
            piocheBateau.setFitHeight(175);
            piocheBateau.setPreserveRatio(true);
            this.getChildren().add(piocheBateau);
        }

        // ajout des boutons pions
        Button pionsWagon = new Button();
        Button pionsBateau = new Button();

        Image bateau = new Image("images/bouton-pions-bateau.png");
        Image wagon = new Image("images/bouton-pions-wagon.png");
        ImageView pionsbateau = new ImageView(bateau);
        ImageView pionswagon = new ImageView(wagon);
        pionsbateau.setFitHeight(50);
        pionsbateau.setPreserveRatio(true);
        pionswagon.setFitHeight(50);
        pionswagon.setPreserveRatio(true);

        pionsWagon.setGraphic(pionswagon);
        pionsBateau.setGraphic(pionsbateau);

        leftInfo.getChildren().addAll(pionsWagon, pionsBateau);
        leftInfo.setSpacing(20);




        this.setAlignment(Pos.CENTER);
        leftInfo.setAlignment(Pos.CENTER);

        this.setSpacing(20);
        sixCartes.getChildren().addAll(premiereLigne, deuxiemeLigne);
        this.getChildren().add(sixCartes);
    }


    public void createBindings() {
        ((VueDuJeu) getScene().getRoot()).getJeu().cartesTransportVisiblesProperty().addListener(new ListChangeListener<ICarteTransport>() {
            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
                change.next();
                if (change.wasAdded()) {
                    int compteur = 0;
                    for (ICarteTransport cartes : change.getAddedSubList()) {
                        if(compteur%2 == 0){
                            premiereLigne.getChildren().add(new VueCarteTransport(cartes, 1));
                        }else {
                            deuxiemeLigne.getChildren().add(new VueCarteTransport(cartes, 1));
                        }
                        compteur++;
                    }
                }
            }
        });

        ((VueDuJeu) getScene().getRoot()).getJeu().instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                instructions.setText(t1);
            }
        });
    }
}
