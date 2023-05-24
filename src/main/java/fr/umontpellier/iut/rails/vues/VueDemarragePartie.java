package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VueDemarragePartie extends HBox {

    private VBox instructionsEtPasser;
    private HBox choixDesDestinations;
    private HBox ecrire;
    private Button passer;
    private Button valider;
    private Label instruction;
    private TextField zoneDeTexte;

    public VueDemarragePartie() {
        instructionsEtPasser = new VBox();
        choixDesDestinations = new HBox();
        ecrire = new HBox();
        valider = new Button("Valider");
        passer = new Button("Passer");
        instruction = new Label();
        zoneDeTexte = new TextField();

        ecrire.getChildren().addAll(zoneDeTexte, valider);
        this.getChildren().add(instructionsEtPasser);
        this.getChildren().add(choixDesDestinations);
        instructionsEtPasser.getChildren().addAll(instruction, passer);

        this.setSpacing(20);
        instructionsEtPasser.setSpacing(20);
        choixDesDestinations.setSpacing(20);
        ecrire.setSpacing(10);
    }

    public void createBindings() {
        ((VueDuJeu) getScene().getRoot()).getJeu().instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                instruction.setText(t1);
            }
        });

        passer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                instructionsEtPasser.getChildren().remove(passer);
                getChildren().add(ecrire);
            }
        });

        zoneDeTexte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().leNombreDePionsSouhaiteAEteRenseigne(zoneDeTexte.getText());
            }
        });

        valider.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().leNombreDePionsSouhaiteAEteRenseigne(zoneDeTexte.getText());
            }
        });
    }
}
