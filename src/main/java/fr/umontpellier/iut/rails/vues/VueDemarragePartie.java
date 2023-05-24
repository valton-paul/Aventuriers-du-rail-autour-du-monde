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

    public VueDemarragePartie(IJeu jeu)
    {
        VBox instructionsEtPasser = new VBox();
        HBox choixDesDestinations = new HBox();
        HBox ecrire = new HBox();
        Button ok = new Button("Commencer");
        Button valider = new Button("Valider");
        Label instruction = new Label();
        TextField zoneDeTexte = new TextField();

        ecrire.getChildren().addAll(zoneDeTexte, valider);
        this.getChildren().add(instructionsEtPasser);
        this.getChildren().add(choixDesDestinations);
        instructionsEtPasser.getChildren().addAll(instruction, ok);

        this.setSpacing(20);
        instructionsEtPasser.setSpacing(20);
        choixDesDestinations.setSpacing(20);
        ecrire.setSpacing(10);

        jeu.instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                instruction.setText(s);
            }
        });

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                instructionsEtPasser.getChildren().remove(ok);
                getChildren().add(ecrire);
            }
        });

        zoneDeTexte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                jeu.leNombreDePionsSouhaiteAEteRenseigne(zoneDeTexte.getText());
            }
        });

        valider.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                jeu.leNombreDePionsSouhaiteAEteRenseigne(zoneDeTexte.getText());
            }
        });

    }
}
