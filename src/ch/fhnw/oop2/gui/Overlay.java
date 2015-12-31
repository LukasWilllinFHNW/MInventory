package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Lukas on 15.12.2015.
 */
public class Overlay extends Stage implements ViewTemplate {

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private StackPane stackPane;
    private VBox container;
    private Scene scene;

    Button backButton;


    // --- CONSTRUCTORS ---
    public Overlay(MInventoryPresentationModel presModel, MInventoryDataModel dataModel, double height, double width){

        this.presModel = presModel;
        this.dataModel = dataModel;

        initSequence();

        super.setHeight(height);
        super.setWidth(width);

        //super.setX(presModel.getX() + (presModel.getWidthProperty().get()/2) - (super.getWidth()/2) );
        //super.setY(presModel.getY() + (presModel.getHeightProperty().get()/2) - (super.getHeight()/2) );

        this.initStyle(StageStyle.TRANSPARENT);

        HBox box = new HBox();
            box.getChildren().add(backButton);
            box.setPadding(new Insets(5));
        addNode(box);

        this.open();

    }


    // --- API ---
    public void addNode(Node node) {
        container.getChildren().add(node);
    }

    public void close() {
        super.close();
    }

    public void open() {
        super.show();
    }


    // --- UI INIT ---
    @Override
    public void initializeControls() {
        backButton = new Button("<-");
    }

    @Override
    public void initializeLayout() {

        container = new VBox();
            //container.setFillWidth(true);
            //container.autosize();

        stackPane = new StackPane(container);
            stackPane.setAlignment(Pos.TOP_CENTER);
            //stackPane.autosize();
    }

    @Override
    public void layoutPanes() {
        scene = new Scene(stackPane, Color.TRANSPARENT);

        this.setScene(scene);
    }

    @Override
    public void layoutControls() {
    }

    @Override
    public void addListeners() {
    }

    @Override
    public void addEvents() {
        backButton.setOnMouseClicked(event -> {
            presModel.enterEditMode();
            close();
        });
    }

    @Override
    public void applyStylesheet() {

        scene.getStylesheets()
            .add(getClass()
                .getResource("appStylesheet.css")
                .toExternalForm() );

        stackPane.setId("overlay_Background");
        container.setId("overlay_Container");
    }

    @Override
    public void applySpecialStyles() {
        presModel.doBlur();
    }



}
