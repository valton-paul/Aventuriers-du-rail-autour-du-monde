package fr.umontpellier.iut.rails.vues;


import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.data.Ville;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {
    private ArrayList<IJoueur> listeJoueurOrdre;
    private static HashMap<String, String> imagesDesJoueurs;
    public VueAutresJoueurs()
    {
        this.setSpacing(20);
        //this.setPadding(new Insets(0, 10 ,0 ,10));
    }

    public static void setImages(HashMap<String, String> images)
    {
        imagesDesJoueurs = new HashMap<>();
        imagesDesJoueurs.putAll(images);
    }

    public void createBindings()
    {
        listeJoueurOrdre = new ArrayList<>(((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs());
        creationVBoxJoueurSaufCourant();
        ChangeListener<IJoueur> joueurCourant = new ChangeListener<IJoueur>() {

            @Override
            public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur iJoueur, IJoueur t1) {
                getChildren().clear();
                listeJoueurOrdre.remove(t1);
                creationVBoxJoueurSaufCourant();
                listeJoueurOrdre.add(t1);
                String lienImg = "";
                for(Map.Entry<String, String> entry : imagesDesJoueurs.entrySet())
                {
                    if(entry.getKey().equals(((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().get().getNom()))
                    {
                        lienImg = entry.getValue();
                        break;
                    }
                }
                VueJoueurCourant vjc = new VueJoueurCourant(((VueDuJeu) getScene().getRoot()).getJeu(), lienImg);
                Color backgroundColor = Color.web(getCouleur(t1));
                Color colorWithOpacity = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 0.5);
                BackgroundFill backgroundFill = new BackgroundFill(colorWithOpacity, null, null);
                Background background = new Background(backgroundFill);
                vjc.setBackground(background);
                getChildren().add(0, vjc);
            }
        };
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(joueurCourant);
    }

    public void creationVBoxJoueurSaufCourant()
    {
        for (IJoueur j : listeJoueurOrdre)
        {
            VBox principal = new VBox();
            HBox profil = new HBox();
            for(Map.Entry<String, String> entry : imagesDesJoueurs.entrySet())
            {
                if(entry.getKey().equals(j.getNom()))
                {
                    ImageView img = new ImageView(new Image("images/cartesWagons/"+entry.getValue()+".png"));
                    img.setFitHeight(50);
                    img.setFitWidth(40);
                    profil.getChildren().add(img);
                }
            }
            principal.getChildren().add(profil);
            this.getChildren().add(principal);
            Label label = new Label(j.getNom() + " | " + j.getScore() + " points");
            Label pionsWagon = new Label("Pions wagons : "+j.getNbPionsWagon());
            Label pionsBateau = new Label("Pions bateaux : "+j.getNbPionsBateau());
            label.setTextFill(Color.web(getCouleur(j)));
            VBox destinations = new VBox();
            for (IDestination d : j.getDestinations()){
                StringBuilder villesDest = new StringBuilder("-> ");
                for(String v : d.getVilles()){
                    villesDest.append(v).append(" - ");
                }
                villesDest.replace(villesDest.length()-3, villesDest.length()-1, ""); //Permet d'enlever le dernier tiret
                Label uneDestination = new Label(villesDest.toString());
                destinations.getChildren().add(uneDestination);
                uneDestination.setOnMouseEntered(e->{
                    trouverVilles(d.getVilles(), true);
                });
                uneDestination.setOnMouseExited(e->{
                    trouverVilles(d.getVilles(), false);
                });
            }
            principal.getChildren().addAll(pionsWagon, pionsBateau);
            profil.getChildren().add(label);
            label.setFont(new Font("Arial", 20));
            principal.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    principal.getChildren().add(destinations);
                }
            });
            principal.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    principal.getChildren().remove(destinations);
                }
            });
        }
    }

    public static HashMap<String, String> getImagesDesJoueurs() {
        return imagesDesJoueurs;
    }

    public static String getCouleur(IJoueur j)
    {
        String color = "";
        switch (j.getCouleur())
        {
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
