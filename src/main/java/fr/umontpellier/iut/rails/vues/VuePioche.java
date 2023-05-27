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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;

public class VuePioche extends VBox {
    private HBox toutSafuInstruction;
    private VBox sixCartes;
    private HBox premiereLigne;
    private HBox deuxiemeLigne;
    private Label instructions;
    private Button pionsWagon;
    private Button pionsBateau;
    private Button piocheWagonButton;
    private Button piocheBateauButton;
    private TextField saisiePions;


    public VuePioche(IJeu jeu) {
        // on ajoute les 6 cartes de la pioches visibles
        sixCartes = new VBox();
        premiereLigne = new HBox();
        deuxiemeLigne = new HBox();
        instructions = new Label();
        instructions.setFont(new Font("Arial", 30));
        piocheWagonButton = new Button();
        piocheBateauButton = new Button();
        saisiePions = new TextField();
        saisiePions.setStyle("-fx-border-color: black; -fx-border-radius: 0px; -fx-font-size: 20;");
        saisiePions.setMaxWidth(100);
        saisiePions.setMinHeight(50);
        saisiePions.setAlignment(Pos.CENTER);

        toutSafuInstruction = new HBox();
        toutSafuInstruction.setStyle("-fx-border-color: black; -fx-border-radius: 5px;");
        toutSafuInstruction.setSpacing(10);
        premiereLigne.setSpacing(10);
        deuxiemeLigne.setSpacing(10);



        this.getChildren().add(instructions);
        this.getChildren().add(toutSafuInstruction);


        // ajout des boutons pions
        pionsWagon = new Button();
        pionsBateau = new Button();

        Image bateau = new Image("images/bouton-pions-bateau.png");
        Image wagon = new Image("images/bouton-pions-wagon.png");
        ImageView pionsbateau = new ImageView(bateau);
        ImageView pionswagon = new ImageView(wagon);
        pionsbateau.setFitHeight(75);
        pionsbateau.setPreserveRatio(true);
        pionswagon.setFitHeight(75);
        pionswagon.setPreserveRatio(true);

        pionsWagon.setGraphic(pionswagon);
        pionsBateau.setGraphic(pionsbateau);

        VBox les2Pions = new VBox();
        les2Pions.setSpacing(10);
        les2Pions.setAlignment(Pos.CENTER);
        les2Pions.getChildren().addAll(pionsWagon, pionsBateau);

        toutSafuInstruction.getChildren().add(les2Pions);


        // on ajoute les pioches
        if (!jeu.piocheWagonVideProperty().getValue()) {
            Image wagoncarte = new Image("images/cartesWagons/dos-WAGON.png");
            ImageView piocheWagon = new ImageView(wagoncarte);
            piocheWagon.setFitHeight(175);
            piocheWagon.setPreserveRatio(true);
            piocheWagonButton.setGraphic(piocheWagon);
            toutSafuInstruction.getChildren().add(piocheWagonButton);

        }

        if (!jeu.piocheBateauVideProperty().getValue()) {
            Image bateaucarte = new Image("images/cartesWagons/dos-BATEAU.png");
            ImageView piocheBateau = new ImageView(bateaucarte);
            piocheBateau.setFitHeight(175);
            piocheBateau.setPreserveRatio(true);
            piocheBateauButton.setGraphic(piocheBateau);
            toutSafuInstruction.getChildren().add(piocheBateauButton);
        }


        toutSafuInstruction.setSpacing(20);


        toutSafuInstruction.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        toutSafuInstruction.setSpacing(20);
        sixCartes.getChildren().addAll(premiereLigne, deuxiemeLigne);
        toutSafuInstruction.getChildren().add(sixCartes);
    }


    public void createBindings() {

        saisiePions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!saisiePions.getText().isEmpty()) {
                    IJoueur avantEchange = ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().get();
                    ((VueDuJeu) getScene().getRoot()).getJeu().leNombreDePionsSouhaiteAEteRenseigne(saisiePions.getText());
                    if(!((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().get().equals(avantEchange)) getChildren().remove(1);
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
            if(!this.getChildren().contains(saisiePions))this.getChildren().add(1, saisiePions);
        });

        pionsBateau.setOnAction(actionEvent ->
        {
            ((VueDuJeu) getScene().getRoot()).getJeu().nouveauxPionsBateauxDemandes();
            if(!this.getChildren().contains(saisiePions))this.getChildren().add(1, saisiePions);
        });

        ((VueDuJeu) getScene().getRoot()).getJeu().cartesTransportVisiblesProperty().addListener(new ListChangeListener<ICarteTransport>() {
            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
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

        ((VueDuJeu) getScene().getRoot()).getJeu().instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                instructions.setText(t1);
            }
        });
    }
}
