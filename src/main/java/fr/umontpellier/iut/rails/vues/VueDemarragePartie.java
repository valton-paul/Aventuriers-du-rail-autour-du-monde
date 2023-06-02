package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class VueDemarragePartie extends HBox {

    private VBox instructionsEtPasser;
    private HBox choixDesDestinations;
    private HBox ecrire;
    private Button garder;
    private Button passer;
    private Button valider;
    private Label instruction;
    private TextField zoneDeTexte;
    public ListChangeListener<IDestination> dest;

    public VueDemarragePartie() {
        instructionsEtPasser = new VBox();
        choixDesDestinations = new HBox();
        ecrire = new HBox();
        valider = new Button("Valider");
        garder = new Button("Passer");
        instruction = new Label();
        zoneDeTexte = new TextField();
        passer = new Button("Passer");

        ecrire.getChildren().addAll(zoneDeTexte, valider);
        this.getChildren().add(instructionsEtPasser);
        this.getChildren().add(choixDesDestinations);
        instructionsEtPasser.getChildren().add(garder);
        garder.setStyle("-fx-background-color: black; -fx-color-label-visible: white; -fx-border-radius: 20px;");
        garder.setTextFill(Color.WHITE);
        garder.setPadding(new Insets(20, 40, 20, 40));
        this.setStyle("-fx-border-width: 3px 0 0 0; -fx-border-color: black;");
        this.setPadding(new Insets(50));
        this.setAlignment(Pos.CENTER);


            this.setSpacing(20);
        instructionsEtPasser.setSpacing(20);
        choixDesDestinations.setSpacing(20);
        ecrire.setSpacing(10);
    }

    public void createBindings() {

        this.setPrefWidth(getScene().getWidth());


        ((VueDuJeu) getScene().getRoot()).getJeu()
                .instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                instruction.setText(t1);
            }
        });

        garder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().passerAEteChoisi();
            }
        });

        dest = new ListChangeListener<IDestination>() {
            @Override
            public void onChanged(Change<? extends IDestination> change) {
                change.next();
                if (change.wasAdded()) {
                    for (IDestination destination : change.getAddedSubList()) {
                        VueDestination d = new VueDestination(destination);
                        d.createBindings();
                        choixDesDestinations.getChildren().add(d);
                    }
                } else if (change.wasRemoved()) {
                    ArrayList<Node> removeList = new ArrayList<>();
                    for (IDestination destination : change.getRemoved()) {
                        for (Node node : choixDesDestinations.getChildren()) {
                            VueDestination v = (VueDestination) node;
                            if (v.getDestination().equals(destination)) removeList.add(node);
                        }
                    }
                    choixDesDestinations.getChildren().removeAll(removeList);
                }
                if (((VueDuJeu) getScene().getRoot()).getJeu().destinationsInitialesProperty().isEmpty() && !getChildren().contains(ecrire)) {
                    getChildren().add(ecrire);
                }
            }
        };
        ((VueDuJeu) getScene().getRoot()).getJeu().destinationsInitialesProperty().addListener(dest);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().leNombreDePionsSouhaiteAEteRenseigne(zoneDeTexte.getText());
                if (!getChildren().contains(choixDesDestinations)) getChildren().add(choixDesDestinations);
            }
        };
        zoneDeTexte.setOnAction(event);
        valider.setOnAction(event);


    }
}
