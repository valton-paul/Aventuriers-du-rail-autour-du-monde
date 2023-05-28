package fr.umontpellier.iut.rails.vues;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 * <p>
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {


    private final ObservableList<String> nomsJoueurs;

    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }

    private HBox container;
    private ArrayList<ImageView> visages;
    private ArrayList<Integer> imagesDejaPrises;
    private Button ajouter;
    private BorderPane gandefentre;
    private boolean tousRempli;
    private StackPane bouton;

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        container = new HBox();
        visages = new ArrayList<>();
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-BLEU.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-ROUGE.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-ROSE.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-JAUNE.png")));
        visages.add(new ImageView(new Image("images/cartesWagons/avatar-VERT.png")));
        imagesDejaPrises = new ArrayList<>();
        ajouter = new Button();
        gandefentre = new BorderPane();
        tousRempli = true;
        bouton = new StackPane();

        gandefentre.setCenter(container);
        Button lancer = new Button("Lancer la partie !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        lancer.setPadding(new Insets(5));
        VBox bas = new VBox(lancer);
        bas.setAlignment(Pos.CENTER);
        bas.setPadding(new Insets(30));
        BorderStrokeStyle borderStyle = BorderStrokeStyle.SOLID;
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, borderStyle, null, new BorderWidths(5, 0, 0, 0));
        bas.setBorder(new Border(borderStroke));
        gandefentre.setBottom(bas);
        ImageView image = new ImageView(new Image("images/addButton.png"));
        image.setFitHeight(115);
        image.setFitWidth(125);
        ajouter.setGraphic(image);

        ArrayList<String> noms = new ArrayList<>();
        lancer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                tousRempli = true;
                for (Node n : container.getChildren()) {
                    if (n instanceof VBox box) {
                        for (Node element : box.getChildren()) {
                            if (element instanceof TextField) {
                                if (!((TextField) element).getText().isEmpty()) {
                                    noms.add(((TextField) element).getText());
                                }else{
                                    tousRempli = false;
                                }
                            }

                        }
                    }
                }
                if(tousRempli){
                    nomsJoueurs.addAll(noms);
                    close();
                }
            }
        });

        ajouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (imagesDejaPrises.size() < 6) {
                    creerJoueur();
                }
                if (imagesDejaPrises.size() == 5) {
                    container.getChildren().remove(bouton);
                }
            }
        });

        Scene s = new Scene(gandefentre);
        this.setScene(s);

        bouton.getChildren().add(ajouter);
        bouton.setAlignment(Pos.CENTER);
        bouton.setPadding(new Insets(0, 0, 0, 75));
        container.getChildren().add(bouton);
        for (int i = 0; i < 2; i++) {
            creerJoueur();
        }


        container.setAlignment(Pos.CENTER);
        this.setMaximized(true);
        this.show();
    }


    public void creerJoueur() {
        VBox unJoueur = new VBox();
        BorderStrokeStyle borderStyle = BorderStrokeStyle.SOLID;
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, borderStyle, null, new BorderWidths(0, 5, 0, 5));
        unJoueur.setBorder(new Border(borderStroke));
        unJoueur.setPadding(new Insets(20));
        unJoueur.setSpacing(50);
        unJoueur.setMaxWidth(this.getWidth() / 6);

        unJoueur.setAlignment(Pos.CENTER);
        Random chiffre = new Random();
        int randomNumber = chiffre.nextInt(5);
        while (imagesDejaPrises.contains(randomNumber)) {
            chiffre = new Random();
            randomNumber = chiffre.nextInt(5);
        }
        unJoueur.getChildren().add(visages.get(randomNumber));
        imagesDejaPrises.add(randomNumber);

        unJoueur.getChildren().add(new Label("Nom du joueur : "));
        TextField t = new TextField();
        t.setAlignment(Pos.CENTER);
        unJoueur.getChildren().add(t);

        Button enelever = new Button();
        ImageView img = new ImageView(new Image("images/removeButton.png"));
        img.setFitHeight(115);
        img.setFitWidth(125);
        enelever.setGraphic(img);
        unJoueur.getChildren().add(enelever);

        container.getChildren().add(container.getChildren().size() - 1, unJoueur);
        int finalRandomNumber = randomNumber;
        enelever.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (container.getChildren().size() > 3) {
                    container.getChildren().remove(unJoueur);
                    imagesDejaPrises.remove((Object) finalRandomNumber);
                }
                if (!container.getChildren().contains(bouton)) {
                    container.getChildren().add(bouton);
                    bouton.setPadding(new Insets(0, 0, 0, 75));
                }
            }
        });
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }

    /**
     * Définit l'action à exécuter lorsque le nombre de participants change
     */
    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Integer> quandLeNombreDeJoueursChange) {
    }

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs(); i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            } else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        throw new RuntimeException("Methode à implémenter");
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     *
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        throw new RuntimeException("Methode à implémenter");
    }

}
