package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import com.sun.javafx.css.Style;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Lukas on 15.12.2015.
 */
public class Overlay extends Stage implements ViewTemplate {

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private VBox container;
    private VBox virtualContainer;
    private Scene scene;


    // --- CONSTRUCTORS ---
    public Overlay(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        initSequence();

        this.initStyle(StageStyle.TRANSPARENT);
        this.setMinWidth(presModel.getWidthProperty().get());
        this.setMinHeight(presModel.getHeightProperty().get());
        this.setMaxWidth(presModel.getWidthProperty().get());
        this.setMaxHeight(presModel.getHeightProperty().get());


        this.toFront();
        this.show();
    }


    // --- API ---
    public void addNode(Node node) {

        node.opacityProperty().setValue(1f);
        node.autosize();
        this.container.getChildren().add(node);
        updateLayout();
    }

    private void updateLayout() {
        updateSize(); // size needs to be evaluated first

        double x = presModel.getX();
        double y = presModel.getY();

        x += (presModel.getWidthProperty().getValue()/2);
        x -= (this.getWidth()/2);
        y += (presModel.getHeightProperty().getValue()/2);
        y -= (this.getHeight()/2);

        this.setX(x);
        this.setY(y);
    }

    private void updateSize() {
        // TODO: Implement proper resising
        double h = presModel.getHeightProperty().get()-200;
        double w = presModel.getWidthProperty().get()-300;

        for (Node n : container.getChildren()) {
            h += n.getBoundsInParent().getHeight();
            if (w < n.getBoundsInParent().getWidth()) {
                //w = n.getBoundsInParent().getWidth();
            }
        }
        this.setHeight(h);
        this.setWidth(w);
    }


    // --- UI INIT ---
    public void initializeControls() {

    }

    public void initializeLayout() {


        virtualContainer = new VBox();
            virtualContainer.setMinSize(Double.MAX_VALUE-1, Double.MAX_VALUE-1);
            virtualContainer.setPrefSize(Double.MAX_VALUE-1, Double.MAX_VALUE-1);

        presModel.doBlur();

        container = new VBox();
            container.setAlignment(Pos.CENTER);

        scene = new Scene(container);
    }

    public void layoutPanes() {
        this.setScene(scene);
    }

    public void layoutControls() {

    }

    @Override
    public void addListeners() {
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                presModel.enterEditMode();
                this.close();
            } });
    }

    @Override
    public void addEvents() {
        this.container.setOnMouseClicked(event -> {
            presModel.enterEditMode();
            this.close(); });
    }

    @Override
    public void applyStylesheet() {
        Rectangle rect = new Rectangle();
            rect.setId("roundedShape");
        container.setShape(rect);
        container.setId("overlay_Container");
        container.setStyle("overlay_Container");

        this.opacityProperty().setValue(1f);
        container.opacityProperty().setValue(1f);
    }



}
