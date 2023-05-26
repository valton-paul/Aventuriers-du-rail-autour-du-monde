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
import javafx.scene.text.Font;

import java.io.File;

public class VuePioche extends VBox {
    private HBox toutSafuInstruction;
    private VBox sixCartes;
    private HBox premiereLigne;
    private HBox deuxiemeLigne;
    private Label instructions;

    public VuePioche(IJeu jeu) {
        // on ajoute les 6 cartes de la pioches visibles
        sixCartes = new VBox();
        premiereLigne = new HBox();
        deuxiemeLigne = new HBox();
        instructions = new Label();
        instructions.setFont(new Font("Arial", 30));

        toutSafuInstruction = new HBox();

        this.getChildren().add(instructions);
        this.getChildren().add(toutSafuInstruction);


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

        VBox les2Pions = new VBox();
        les2Pions.setAlignment(Pos.CENTER);
        les2Pions.getChildren().addAll(pionsWagon, pionsBateau);

        toutSafuInstruction.getChildren().add(les2Pions);



        // on ajoute les pioches
        if (!jeu.piocheWagonVideProperty().getValue()) {
            Image wagoncarte = new Image("images/cartesWagons/dos-WAGON.png");
            ImageView piocheWagon = new ImageView(wagoncarte);
            piocheWagon.setFitHeight(175);
            piocheWagon.setPreserveRatio(true);
            toutSafuInstruction.getChildren().add(piocheWagon);
        }

        if (!jeu.piocheBateauVideProperty().getValue()) {
            Image bateaucarte = new Image("images/cartesWagons/dos-BATEAU.png");
            ImageView piocheBateau = new ImageView(bateaucarte);
            piocheBateau.setFitHeight(175);
            piocheBateau.setPreserveRatio(true);
            toutSafuInstruction.getChildren().add(piocheBateau);
        }


        toutSafuInstruction.setSpacing(20);




        toutSafuInstruction.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);

        toutSafuInstruction.setSpacing(20);
        sixCartes.getChildren().addAll(premiereLigne, deuxiemeLigne);
        toutSafuInstruction.getChildren().add(sixCartes);
    }


    public void createBindings() {
        ((VueDuJeu) getScene().getRoot()).getJeu().cartesTransportVisiblesProperty().addListener(new ListChangeListener<ICarteTransport>() {
            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
                change.next();
                if (change.wasAdded()) {
                    int compteur = 0;
                    for (ICarteTransport cartes : change.getAddedSubList()) {
                        VueCarteTransport vueTransport = new VueCarteTransport(cartes, 1);
                        vueTransport.createBinding();
                        if(compteur%2 == 0){
                            premiereLigne.getChildren().add(vueTransport);
                        }else {
                            deuxiemeLigne.getChildren().add(vueTransport);
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
