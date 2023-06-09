package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.*;
import fr.umontpellier.iut.rails.mecanique.Jeu;
import fr.umontpellier.iut.rails.mecanique.Route;
import fr.umontpellier.iut.rails.mecanique.data.Ville;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends ScrollPane {
    private final VBox cartesTransport;

    public VueJoueurCourant(IJeu jeu, String lien) {

        VBox principal = new VBox();
        HBox profil = new HBox();
        ImageView img = new ImageView(new Image("images/cartesWagons/" + lien + ".png"));
        img.setFitHeight(75);
        img.setFitWidth(60);
        profil.getChildren().add(img);
        cartesTransport = new VBox();
        principal.getChildren().add(profil);
        this.setContent(principal);

        Label label = new Label(jeu.joueurCourantProperty().get().getNom() + " | " + jeu.joueurCourantProperty().get().getScore() + " points");
        label.setTextFill(Color.web(getCouleur(jeu.joueurCourantProperty().get())));
        label.setFont(new Font("Arial", 30));
        profil.getChildren().add(label);
        //this.setSpacing(20);
        this.setPadding(new Insets(0, 10, 0, 10));
        profil.setAlignment(Pos.CENTER_LEFT);

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
        for (IDestination d : jeu.joueurCourantProperty().get().getDestinations()) {
            StringBuilder villesDest = new StringBuilder("-> ");
            for (String v : d.getVilles()) {
                villesDest.append(v).append(" - ");
            }
            villesDest.replace(villesDest.length() - 3, villesDest.length() - 1, ""); //Permet d'enlever le dernier tiret
            Label uneDestination = new Label(villesDest.toString());
            uneDestination.setOnMouseEntered(event -> {
                trouverVilles(d.getVilles(), true);
                uneDestination.setTextFill(Color.BROWN);
            });
            uneDestination.setOnMouseExited(event -> {
                trouverVilles(d.getVilles(), false);
                uneDestination.setTextFill(Color.BLACK);

            });
            destinations.getChildren().add(uneDestination);
        }
        principal.getChildren().add(cartesTransport);
        principal.getChildren().add(destinations);

        for (ICarteTransport carte : jeu.joueurCourantProperty().get().getCartesTransport()) {
            if (carte.estBateau()) {
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
                    for (ICarteTransport carte : change.getAddedSubList()) {
                        VueCarteTransport vue = new VueCarteTransport(carte, 1, true);
                        vue.createBinding();
                        boolean ajouter = false;
                        for (Node node : cartesTransport.getChildren()) {
                            HBox lesCartes = (HBox) node;
                            if (lesCartes.getChildren().size() < 4) {
                                lesCartes.getChildren().add(vue);
                                ajouter = true;
                                break;
                            }
                        }
                        if (!ajouter) {
                            HBox lesCartes = new HBox();
                            lesCartes.getChildren().add(vue);
                            cartesTransport.getChildren().add(lesCartes);
                        }
                    }
                } else if (change.wasRemoved()) {
                    for (ICarteTransport carte : change.getRemoved()) {
                        all:
                        for (Node node : cartesTransport.getChildren()) {
                            HBox hbox = (HBox) node;
                            for (Node node1 : hbox.getChildren()) {
                                VueCarteTransport vue = (VueCarteTransport) node1;
                                if (carte.equals(vue.getCarteTransport())) hbox.getChildren().remove(vue);
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


    public void trouverVilles(List<String> villes, boolean briller) {
        ArrayList<Ville> toutesLesVilles = new ArrayList<>();
        for (Node s : ((VueDuJeu) getScene().getRoot()).getPlateau().getChildren()) {
            if (s instanceof Circle) {
                if(villes.contains(s.getId())){
                    if(briller){
                        s.setOpacity(1);
                        ((Circle) s).setFill(Color.GOLD);
                    }
                    else s.setOpacity(0);
                }

            }
        }
    }
}
