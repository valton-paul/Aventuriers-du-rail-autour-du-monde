package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.mecanique.data.Ville;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Destination.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueDestination extends Button {

    private final IDestination destination;

    public VueDestination(IDestination destination) {
        this.destination = destination;
        StringBuilder nomDestination = new StringBuilder();
        for (String nomVille : destination.getVilles())
        {
            nomDestination.append(nomVille).append(" - ");
        }
        nomDestination.append("\n Points : ").append(destination.getValeur());
        this.setText(nomDestination.toString());
    }

    public IDestination getDestination() {
        return destination;
    }

}
