package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends Button {

    private final ICarteTransport carteTransport;

    public VueCarteTransport(ICarteTransport carteTransport, int nbCartes, boolean main) {
        this.carteTransport = carteTransport;

        StringBuilder nomCarte = new StringBuilder().append("carte-");
        if(carteTransport.estBateau()) nomCarte.append("BATEAU-");
        else if(carteTransport.estDouble()) nomCarte.append("DOUBLE-");
        else if(carteTransport.estJoker()) nomCarte.append("JOKER-");
        else if(carteTransport.estWagon()) nomCarte.append("WAGON-");

        nomCarte.append(carteTransport.getStringCouleur().toUpperCase());

        if(carteTransport.getAncre()) nomCarte.append("-A.png");
        else nomCarte.append(".png");


        ImageView img = new ImageView(new Image("images/cartesWagons/" + nomCarte));
        if(main)img.setFitHeight(50);
        else img.setFitHeight(100);
        img.setPreserveRatio(true);
        this.setGraphic(img);
    }

    public void createBinding()
    {
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteTransportAEteChoisie(carteTransport);
            }
        });
    }



    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }


}
