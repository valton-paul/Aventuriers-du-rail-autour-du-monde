package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
    private Button garder;
    private Button passer;
    private Button valider;
    private Label instruction;
    private TextField zoneDeTexte;

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
        instructionsEtPasser.getChildren().addAll(instruction, garder);

        this.setSpacing(20);
        instructionsEtPasser.setSpacing(20);
        choixDesDestinations.setSpacing(20);
        ecrire.setSpacing(10);
    }

    public void createBindings() {
        ((VueDuJeu) getScene().getRoot()).getJeu().jeuEnPreparationProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                getChildren().remove(choixDesDestinations);
                getChildren().remove(ecrire);
            }
        });

        ((VueDuJeu) getScene().getRoot()).getJeu().instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                instruction.setText(t1);
            }
        });

        garder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                instructionsEtPasser.getChildren().remove(garder);
                getChildren().remove(choixDesDestinations);
                getChildren().add(ecrire);
                ((VueDuJeu) getScene().getRoot()).getJeu().passerAEteChoisi();
            }
        });

        ((VueDuJeu) getScene().getRoot()).getJeu().destinationsInitialesProperty().addListener(new ListChangeListener<IDestination>() {
            @Override
            public void onChanged(Change<? extends IDestination> change) {
                change.next();
                if(change.wasAdded())
                {
                    for(IDestination destination : change.getAddedSubList())
                    {
                        choixDesDestinations.getChildren().add(new VueDestination(destination));
                    }
                }
                else if(change.wasRemoved())
                {
                    choixDesDestinations.getChildren().clear();
                }
            }
        });

        zoneDeTexte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().leNombreDePionsSouhaiteAEteRenseigne(zoneDeTexte.getText());
                instructionsEtPasser.getChildren().add(garder);
                getChildren().remove(ecrire);
                getChildren().add(choixDesDestinations);
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
