package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class VuePioche extends HBox {
    private HBox toutSafuInstruction;
    private VBox sixCartes;
    private HBox premiereLigne;
    private HBox deuxiemeLigne;
    private Button pionsWagon;
    private Button pionsBateau;
    private Button piocheWagonButton;
    private Button piocheBateauButton;
    private TextField saisiePions;
    private Button piocheDest;
    private VBox toutesLesDest;
    private Button passer;
    private Button allerALaFin;
    private ScrollPane scrollDest;

    private VBox instructionetPions;


    public VuePioche(IJeu jeu) {
        // on ajoute les 6 cartes de la pioches visibles
        sixCartes = new VBox();
        premiereLigne = new HBox();
        deuxiemeLigne = new HBox();
        piocheWagonButton = new Button();
        piocheBateauButton = new Button();
        saisiePions = new TextField();
        saisiePions.setStyle("-fx-border-color: black; -fx-border-radius: 0px; -fx-font-size: 20;");
        saisiePions.setMaxWidth(100);
        saisiePions.setMinHeight(50);
        saisiePions.setAlignment(Pos.CENTER);
        piocheDest = new Button();
        passer = new Button("Passer");
        instructionetPions = new VBox();
        instructionetPions.setAlignment(Pos.CENTER);
        scrollDest = new ScrollPane();
        toutSafuInstruction = new HBox();
        this.setStyle("-fx-border-width: 3px 0 0 0; -fx-border-color: black;");
        toutSafuInstruction.setSpacing(10);
        premiereLigne.setSpacing(10);
        deuxiemeLigne.setSpacing(10);


        this.getChildren().add(toutSafuInstruction);
        this.getChildren().add(instructionetPions);


        // ajout des boutons pions
        pionsWagon = new Button();
        pionsBateau = new Button();

        Image bateau = new Image("images/bouton-pions-bateau.png");
        Image wagon = new Image("images/bouton-pions-wagon.png");
        ImageView pionsbateau = new ImageView(bateau);
        ImageView pionswagon = new ImageView(wagon);
        pionsbateau.setFitHeight(65);
        pionsbateau.setPreserveRatio(true);
        pionswagon.setFitHeight(65);
        pionswagon.setPreserveRatio(true);

        pionsWagon.setGraphic(pionswagon);
        pionsBateau.setGraphic(pionsbateau);

        VBox les2Pions = new VBox();
        les2Pions.setSpacing(10);
        les2Pions.setAlignment(Pos.CENTER);
        les2Pions.getChildren().addAll(pionsWagon, pionsBateau);

        toutSafuInstruction.getChildren().add(les2Pions);

        ImageView dest = new ImageView(new Image("images/cartesWagons/destinations.png"));
        piocheDest.setGraphic(dest);
        dest.setFitHeight(150);
        dest.setPreserveRatio(true);

        toutSafuInstruction.getChildren().add(piocheDest);


        // on ajoute les pioches
        if (!jeu.piocheWagonVideProperty().getValue()) {
            Image wagoncarte = new Image("images/cartesWagons/dos-WAGON.png");
            ImageView piocheWagon = new ImageView(wagoncarte);
            piocheWagon.setFitHeight(150);
            piocheWagon.setPreserveRatio(true);
            piocheWagonButton.setGraphic(piocheWagon);
            toutSafuInstruction.getChildren().add(piocheWagonButton);

        }

        if (!jeu.piocheBateauVideProperty().getValue()) {
            Image bateaucarte = new Image("images/cartesWagons/dos-BATEAU.png");
            ImageView piocheBateau = new ImageView(bateaucarte);
            piocheBateau.setFitHeight(150);
            piocheBateau.setPreserveRatio(true);
            piocheBateauButton.setGraphic(piocheBateau);
            toutSafuInstruction.getChildren().add(piocheBateauButton);
        }


        toutSafuInstruction.setSpacing(20);


        toutSafuInstruction.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);

        toutSafuInstruction.setSpacing(35);
        sixCartes.getChildren().addAll(premiereLigne, deuxiemeLigne);
        toutSafuInstruction.getChildren().add(sixCartes);
        toutSafuInstruction.getChildren().add(passer);
        passer.setStyle("-fx-padding: 25px 40px; -fx-background-color: #3636b8; -fx-border-radius: 100");
        passer.setFont(new Font("Arial", 30));
        passer.setTextFill(Color.WHITE);
        passer.setOnMouseEntered(event -> this.getScene().setCursor(Cursor.HAND));
        passer.setOnMouseExited(event -> this.getScene().setCursor(Cursor.DEFAULT));



        allerALaFin = new Button("Finir");
        //toutSafuInstruction.getChildren().add(allerALaFin);
    }


    public void createBindings() {
        instructionetPions.setStyle("-fx-border-color: black; -fx-border-width: 0 0 0 3px;");
        instructionetPions.setMaxWidth(getScene().getWidth() - ((VueDuJeu) getScene().getRoot()).getPlateau().getWidth());
        toutSafuInstruction.setMinWidth(((VueDuJeu) getScene().getRoot()).getPlateau().getWidth());



        allerALaFin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().finDePartieProperty().set(true);
            }
        });

        passer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().passerAEteChoisi();
            }
        });

        piocheDest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().nouvelleDestinationDemandee();
                toutesLesDest = new VBox();
                for (IDestination n : ((VueDuJeu) getScene().getRoot()).getJeu().destinationsInitialesProperty()) {
                    VueDestination vu = new VueDestination(n);
                    vu.createBindings();
                    vu.setPrefWidth(instructionetPions.getWidth() - 25);
                    toutesLesDest.getChildren().add(vu);
                }
                scrollDest.setContent(toutesLesDest);
                instructionetPions.getChildren().add(1, scrollDest);
                toutesLesDest.setSpacing(10);
                toutesLesDest.setAlignment(Pos.CENTER);
                toutesLesDest.setPrefHeight(instructionetPions.getHeight() / 2);
                toutesLesDest.setAlignment(Pos.CENTER);
                toutesLesDest.setPadding(new Insets(10));
            }
        });

        ((VueDuJeu) getScene().getRoot()).getJeu().destinationsInitialesProperty().addListener(new ListChangeListener<IDestination>() {
            @Override
            public void onChanged(Change<? extends IDestination> change) {
                change.next();
                if (change.wasRemoved()) {
                    ArrayList<Node> removedList = new ArrayList<>();
                    for (IDestination dest : change.getRemoved()) {

                        for (Node node : toutesLesDest.getChildren()) {
                            VueDestination vu = (VueDestination) node;
                            if (vu.getDestination().equals(dest)) removedList.add(node);
                        }
                    }
                    toutesLesDest.getChildren().removeAll(removedList);
                    if(toutesLesDest.getChildren().isEmpty()) {
                        instructionetPions.getChildren().remove(scrollDest);

                    }
                }
            }
        });

        saisiePions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!saisiePions.getText().isEmpty()) {
                    IJoueur avantEchange = ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().get();
                    ((VueDuJeu) getScene().getRoot()).getJeu().leNombreDePionsSouhaiteAEteRenseigne(saisiePions.getText());
                    if (!((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().get().equals(avantEchange))
                        instructionetPions.getChildren().remove(1);
                }
            }
        });

        piocheWagonButton.setOnAction(actionEvent ->
        {
            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteWagonAEtePiochee();
        });

        piocheBateauButton.setOnAction(actionEvent ->
        {
            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteBateauAEtePiochee();
        });


        pionsWagon.setOnAction(actionEvent ->
        {
            ((VueDuJeu) getScene().getRoot()).getJeu().nouveauxPionsWagonsDemandes();
            if (!instructionetPions.getChildren().contains(saisiePions))
                instructionetPions.getChildren().add(1, saisiePions);
        });

        pionsBateau.setOnAction(actionEvent ->
        {
            ((VueDuJeu) getScene().getRoot()).getJeu().nouveauxPionsBateauxDemandes();
            if (!instructionetPions.getChildren().contains(saisiePions))
                instructionetPions.getChildren().add(1, saisiePions);
        });

        ((VueDuJeu) getScene().getRoot()).getJeu().cartesTransportVisiblesProperty().addListener(new ListChangeListener<ICarteTransport>() {
            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
                premiereLigne.setPadding(new Insets(5));
                deuxiemeLigne.setPadding(new Insets(5));
                change.next();
                if (change.wasAdded()) {
                    for (ICarteTransport cartes : change.getAddedSubList()) {
                        VueCarteTransport vueTransport = new VueCarteTransport(cartes, 1, false);
                        vueTransport.createBinding();
                        if (premiereLigne.getChildren().size() < deuxiemeLigne.getChildren().size()) {
                            premiereLigne.getChildren().add(vueTransport);
                        } else {
                            deuxiemeLigne.getChildren().add(vueTransport);
                        }
                    }
                } else if (change.wasRemoved()) {
                    ArrayList<Node> removeList = new ArrayList<>();
                    for (ICarteTransport carte : change.getRemoved()) {
                        for (Node node : premiereLigne.getChildren()) {
                            VueCarteTransport v = (VueCarteTransport) node;
                            if (v.getCarteTransport().equals(carte)) removeList.add(node);
                        }
                        for (Node node : deuxiemeLigne.getChildren()) {
                            VueCarteTransport v = (VueCarteTransport) node;
                            if (v.getCarteTransport().equals(carte)) removeList.add(node);
                        }
                    }
                    premiereLigne.getChildren().removeAll(removeList);
                    deuxiemeLigne.getChildren().removeAll(removeList);
                }
            }
        });
    }
}
