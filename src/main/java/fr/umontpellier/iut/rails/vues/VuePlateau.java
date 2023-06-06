package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.IRoute;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cette classe présente les routes et les villes sur le plateau.
 * <p>
 * On y définit les handlers à exécuter lorsque qu'un élément du plateau a été choisi par l'utilisateur
 * ainsi que les bindings qui mettront à jour le plateau après la prise d'une route ou d'un port par un joueur
 */
public class VuePlateau extends Pane {

    @FXML
    private ImageView mapMonde;
    private Shape dernierNodeAppuyer;
    private Label instructions;
    private VBox instruct;

    public VuePlateau() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/plateau.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instruct = new VBox();
        setMinSize(Screen.getPrimary().getBounds().getWidth() / 3, Screen.getPrimary().getBounds().getHeight() / 3);
        dernierNodeAppuyer = null;
        instructions = new Label();
        instructions.setFont(new Font("Arial", 30));
        instructions.setScaleZ(3);
        this.setScaleZ(1);
        this.getChildren().add(instruct);
        instruct.getChildren().add(instructions);
        instructions.setTextFill(Color.WHITE);
        instruct.setStyle("-fx-background-color: Black; -fx-opacity: 0.75; -fx-border-radius: 10px; -fx-background-radius: 20px;");
        instructions.setPadding(new Insets(5));
    }

    EventHandler<MouseEvent> choixRoute = event -> {
        System.out.println("On a cliqué sur une route");
    };

    EventHandler<MouseEvent> choixPort = event -> {
        System.out.println("On a cliqué sur un port");
    };

    public void creerBindings() {
        ajouterPorts();
        ajouterRoutes();
        bindRedimensionEtCentragePlateau();
        instruct.setLayoutX(getScene().getWidth()/3.5);
        instruct.setLayoutY(40);

        for(IJoueur j : ((VueDuJeu)getScene().getRoot()).getJeu().getJoueurs())
        {
            j.cartesTransportPoseesProperty().addListener(new ListChangeListener<CarteTransport>() {
                @Override
                public void onChanged(Change<? extends CarteTransport> change) {
                    change.next();
                    if(change.wasRemoved()) {
                        HashMap<Node, Shape> aChanger = new HashMap<>();
                        ArrayList<ImageView> ajout = new ArrayList<>();
                        for (Node node : getChildren()) {
                            if (node instanceof Rectangle rect) {
                                if (rect.getId().equals(dernierNodeAppuyer.getId())) {
                                    rect.setOpacity(1);
                                    rect.setFill(Color.web(VueAutresJoueurs.getCouleur(j)));
//                                    Image img = new Image("images/wagons/image-wagon-" + ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().get().getCouleur().toString().toUpperCase() + ".png");
//                                    //aChanger.put(new ImageView(img), rect);
//                                    for(DonneesGraphiques.DonneesSegments position : DonneesGraphiques.routes.get(rect.getId()))
//                                    {
//                                        ImageView jetons = new ImageView(img);
//                                        ajout.add(jetons);
//                                        jetons.setFitWidth(46);
//                                        jetons.setFitHeight(14);
//                                        jetons.setX(position.getXHautGauche());
//                                        jetons.setY(position.getYHautGauche());
//                                        jetons.setRotate(position.getAngle());
//                                    }
                                }
                            } else if (node instanceof Circle circle) {
                                if (circle.getId().equals(dernierNodeAppuyer.getId()))
                                {
                                    circle.setOpacity(1);
                                    circle.setFill(Color.web(VueAutresJoueurs.getCouleur(j)));
                                }
                            }
                        }
//                        getChildren().addAll(ajout);
//                        for (HashMap.Entry<Node, Shape> entry : aChanger.entrySet()) {
//                            entry.getKey().setLayoutX(entry.getValue().getBoundsInLocal().getCenterX());
//                            entry.getKey().setLayoutX(entry.getValue().getBoundsInLocal().getCenterY());
//                            getChildren().add(getChildren().indexOf(entry.getValue()),entry.getKey());
//                        }
                    }
                }
            });
        }

        TranslateTransition aller = new TranslateTransition(Duration.seconds(.25), instruct);
        TranslateTransition retour = new TranslateTransition(Duration.seconds(.24), instruct);
        SequentialTransition transitions = new SequentialTransition();
        transitions.getChildren().addAll(aller, retour);

        aller.setFromX(0);
        aller.setToX(-3 * getScene().getWidth()/3.5);
        //aller.setNode(null);


        retour.setFromX(-3 * getScene().getWidth()/3.5);
        retour.setToX(0);
        //aller.setNode(instruct);

        ((VueDuJeu)getScene().getRoot()).getJeu().instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                instructions.setText(t1);
                transitions.play();
            }
        });

    }

            private void ajouterPorts() {
                for (String nomPort : DonneesGraphiques.ports.keySet()) {
                    DonneesGraphiques.DonneesCerclesPorts positionPortSurPlateau = DonneesGraphiques.ports.get(nomPort);
                    Circle cerclePort = new Circle(positionPortSurPlateau.centreX(), positionPortSurPlateau.centreY(), DonneesGraphiques.rayonInitial);
                    cerclePort.setId(nomPort);
                    cerclePort.setOpacity(0);
                    getChildren().add(cerclePort);
                    bindCerclePortAuPlateau(positionPortSurPlateau, cerclePort);
                    cerclePort.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override

                        public void handle(MouseEvent mouseEvent) {
                            ((VueDuJeu) getScene().getRoot()).getJeu().unPortAEteChoisi(nomPort);
                            dernierNodeAppuyer = cerclePort;
                        }
                    });
                }
            }

            private void ajouterRoutes() {
                List<? extends IRoute> listeRoutes = ((VueDuJeu) getScene().getRoot()).getJeu().getRoutes();
                for (String nomRoute : DonneesGraphiques.routes.keySet()) {
                    ArrayList<DonneesGraphiques.DonneesSegments> segmentsRoute = DonneesGraphiques.routes.get(nomRoute);
                    IRoute route = listeRoutes.stream().filter(r -> r.getNom().equals(nomRoute)).findAny().orElse(null);
                    for (DonneesGraphiques.DonneesSegments unSegment : segmentsRoute) {
                        Rectangle rectangleSegment = new Rectangle(unSegment.getXHautGauche(), unSegment.getYHautGauche(), DonneesGraphiques.largeurRectangle, DonneesGraphiques.hauteurRectangle);
                        rectangleSegment.setId(nomRoute);
                        rectangleSegment.setRotate(unSegment.getAngle());
                        rectangleSegment.setOpacity(0);
                        getChildren().add(rectangleSegment);
                        bindRectangle(rectangleSegment, unSegment.getXHautGauche(), unSegment.getYHautGauche());
                        rectangleSegment.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                ((VueDuJeu) getScene().getRoot()).getJeu().uneRouteAEteChoisie(nomRoute);
                                dernierNodeAppuyer = rectangleSegment;
                            }
                        });
                    }
                }
            }

            private void bindRedimensionEtCentragePlateau() {
                mapMonde.fitWidthProperty().bind(widthProperty());
                mapMonde.fitHeightProperty().bind(heightProperty());
                mapMonde.layoutXProperty().bind(new DoubleBinding() { // Pour maintenir le plateau au centre
                    {
                        super.bind(widthProperty(), heightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        double imageViewWidth = mapMonde.getLayoutBounds().getWidth();
                        return (getWidth() - imageViewWidth) / 2;
                    }
                });
            }

            private void bindCerclePortAuPlateau(DonneesGraphiques.DonneesCerclesPorts port, Circle cerclePort) {
                cerclePort.centerXProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return mapMonde.getLayoutX() + port.centreX() * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
                    }
                });
                cerclePort.centerYProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return mapMonde.getLayoutY() + port.centreY() * mapMonde.getLayoutBounds().getHeight() / DonneesGraphiques.hauteurInitialePlateau;
                    }
                });
                cerclePort.radiusProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return DonneesGraphiques.rayonInitial * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
                    }
                });
            }

            private void bindRectangle(Rectangle rect, double layoutX, double layoutY) {
                rect.widthProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return DonneesGraphiques.largeurRectangle * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
                    }
                });
                rect.heightProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return DonneesGraphiques.hauteurRectangle * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
                    }
                });
                rect.layoutXProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty(), mapMonde.xProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return mapMonde.getLayoutX() + layoutX * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
                    }
                });
                rect.xProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty(), mapMonde.xProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
                    }
                });
                rect.layoutYProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return layoutY * mapMonde.getLayoutBounds().getHeight() / DonneesGraphiques.hauteurInitialePlateau;
                    }
                });
                rect.yProperty().bind(new DoubleBinding() {
                    {
                        super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return mapMonde.getLayoutBounds().getHeight() / DonneesGraphiques.hauteurInitialePlateau;
                    }
                });
            }

        }