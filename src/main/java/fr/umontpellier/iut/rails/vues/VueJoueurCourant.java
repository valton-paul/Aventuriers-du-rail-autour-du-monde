package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.data.Ville;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends ScrollPane {
    private final VBox cartesTransport;
    public VueJoueurCourant(IJeu jeu) {

        VBox principal = new VBox();
        HBox profil = new HBox();
        cartesTransport = new VBox();
        principal.getChildren().add(profil);
        this.setContent(principal);
        Label label = new Label(jeu.joueurCourantProperty().get().getNom() + " | " + jeu.joueurCourantProperty().get().getScore() + " points");
        label.setTextFill(Color.web(getCouleur(jeu.joueurCourantProperty().get())));
        label.setFont(new Font("Arial", 30));
        profil.getChildren().add(label);
        //this.setSpacing(20);
        this.setPadding(new Insets(0, 10, 0, 5));

        Color backgroundColor = Color.web(getCouleur(jeu.joueurCourantProperty().get()));
        Color colorWithOpacity = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 0.5);
        BackgroundFill backgroundFill = new BackgroundFill(colorWithOpacity, null, null);
        Background background = new Background(backgroundFill);
        principal.setBackground(background);


        // on affiche les pions du joueur (+ le nb de ports)
        VBox pions = new VBox();
        Label nbPionsWagon = new Label("Pions Wagons : " + jeu.joueurCourantProperty().get().getNbPionsWagon());
        Label nbPionsBateau = new Label("Pions Bateaux : " + jeu.joueurCourantProperty().get().getNbPionsBateau());
        Label nbPorts = new Label("Ports : " + jeu.joueurCourantProperty().get().getNbPorts());
        pions.getChildren().addAll(nbPionsWagon, nbPionsBateau, nbPorts);
        principal.getChildren().add(pions);

        // on affiche les destinations
        VBox destinations = new VBox();
        for (IDestination d : jeu.joueurCourantProperty().get().getDestinations()){
            StringBuilder villesDest = new StringBuilder("-> ");
            for(String v : d.getVilles()){
                villesDest.append(v).append(" - ");
            }
            villesDest.replace(villesDest.length()-3, villesDest.length()-1, ""); //Permet d'enlever le dernier tiret
            Label uneDestination = new Label(villesDest.toString());
            uneDestination.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    uneDestination.setStyle("-fx-font-weight: bold");
                }
            });

            uneDestination.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    uneDestination.setStyle("-fx-font-weight: normal");
                }
            });
            destinations.getChildren().add(uneDestination);
        }
        principal.getChildren().add(cartesTransport);
        principal.getChildren().add(destinations);

        for(ICarteTransport carte : jeu.joueurCourantProperty().get().getCartesTransport()){
            if(carte.estBateau()){
                continue;
            }
        }


        VBox vbox = new VBox();
        vbox.setSpacing(10);
        for (ICarteTransport carte : jeu.joueurCourantProperty().get().cartesTransportProperty()) {
            VueCarteTransport vue = new VueCarteTransport(carte, 1, true);
            vue.createBinding();
            vbox.getChildren().add(vue);

            // Vérifier si le nombre de cartes atteint 4
            if (vbox.getChildren().size() % 4 == 0) {
                HBox hbox = new HBox();
                hbox.getChildren().addAll(vbox.getChildren());
                cartesTransport.getChildren().add(hbox);
                vbox.getChildren().clear();
            }
        }

        // Ajouter les cartes restantes
        if (!vbox.getChildren().isEmpty()) {
            HBox hbox = new HBox();
            hbox.getChildren().addAll(vbox.getChildren());
            cartesTransport.getChildren().add(hbox);
        }

        jeu.joueurCourantProperty().get().cartesTransportProperty().addListener(new ListChangeListener<ICarteTransport>() {
            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
                change.next();
                if (change.wasAdded()) {
                    for(ICarteTransport carte : change.getAddedSubList())
                    {
                        VueCarteTransport vue = new VueCarteTransport(carte, 1, true);
                        vue.createBinding();
                        boolean ajouter = false;
                        for(Node node : cartesTransport.getChildren())
                        {
                            HBox lesCartes = (HBox) node;
                            if(lesCartes.getChildren().size() < 4)
                            {
                                lesCartes.getChildren().add(vue);
                                ajouter = true;
                                break;
                            }
                        }
                        if (!ajouter)
                        {
                            HBox lesCartes = new HBox();
                            lesCartes.getChildren().add(vue);
                            cartesTransport.getChildren().add(lesCartes);
                        }
                    }
                }
                else if (change.wasRemoved())
                {
                    System.out.println("remove");
                    for(ICarteTransport carte : change.getRemoved())
                    {
                        all :
                       for(Node node : cartesTransport.getChildren())
                       {
                           HBox hbox = (HBox) node;
                           for(Node node1 : hbox.getChildren())
                           {
                               VueCarteTransport vue = (VueCarteTransport) node1;
                               System.out.println("oui");
                                if(carte.equals(vue.getCarteTransport())) hbox.getChildren().remove(vue);
                               break all;
                           }
                       }
                    }
                }
            }
        });
    }

    public String getCouleur(IJoueur j) {
        String color = "";
        switch (j.getCouleur()) {
            case BLEU -> color = "BLUE";
            case ROSE -> color = "DEEPPINK";
            case VERT -> color = "GREEN";
            case JAUNE -> color = "DARKGOLDENROD";
            case ROUGE -> color = "RED";
        }
        return color;
    }

}
