package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.mecanique.data.Ville;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Cette classe représente la vue d'une carte Destination.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueDestination extends Button {


    private final IDestination destination;
    public VueDestination(IDestination destination) {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));

        this.setOnMouseEntered(event -> this.getScene().setCursor(Cursor.HAND));
        this.setOnMouseExited(event -> this.getScene().setCursor(Cursor.DEFAULT));


        // Créer l'image avec le fond
        Image foregroundImage =  new Image("images/dest.png");

        Background g = new Background(new BackgroundImage(foregroundImage,null, null, null, null));
        //this.setBackground(g);
        this.destination = destination;
        StringBuilder nomDestination = new StringBuilder();
        for (String nomVille : destination.getVilles())
        {
            nomDestination.append(nomVille).append(" - ");
        }
        nomDestination.append("\n Points : ").append(destination.getValeur());
        this.setText(nomDestination.toString());


        //this.setBorder(new Border();

    }

    public void createBindings()
    {
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().uneDestinationAEteChoisie(destination);
            }
        });
    }

    public IDestination getDestination() {
        return destination;
    }

}
