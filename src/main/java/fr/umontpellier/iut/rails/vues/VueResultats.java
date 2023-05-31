package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.RailsIHM;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Cette classe affiche les scores en fin de partie.
 * On peut éventuellement proposer de rejouer, et donc de revenir à la fenêtre principale
 *
 */
public class VueResultats extends BorderPane {

    private RailsIHM ihm;
    private HBox bas;
    private HBox podium;
    private VBox stats;
    private Button rejouer;
    private Button quitter;


    public VueResultats(RailsIHM ihm) {
        this.ihm = ihm;
        bas = new HBox();
        podium = new HBox();
        stats = new VBox();

        this.setBottom(bas);
        this.setCenter(podium);
        this.setRight(stats);


        // on configure le bas
        rejouer = new Button("Rejouer");
        quitter = new Button("Quitter");
        bas.getChildren().addAll(rejouer, quitter);


        // on crééé le podium

        VBox deuxieme = new VBox();
        VBox premier = new VBox();
        VBox troisieme = new VBox();

        Rectangle r2 = new Rectangle();
        Rectangle r1 = new Rectangle();
        Rectangle r3 = new Rectangle();

        r2.setHeight(200);
        r1.setHeight(230);
        r3.setHeight(170);

        r2.setWidth(75);
        r1.setWidth(75);
        r3.setWidth(75);

        r2.setFill(Color.WHITE);
        r1.setFill(Color.GOLD);
        r3.setFill(Color.SILVER);





    }



}
