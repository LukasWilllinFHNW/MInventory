package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by Lukas on 02.12.2015.
 */
public class MInventoryPreviewView extends HBox{

    private MInventoryObject mInventoryObject;

    private Box box;
    private Button logo;
    private Circle circle;

    public MInventoryPreviewView(MInventoryDataModel dataModel){

        this.setAlignment(Pos.CENTER);

        // -- Perform Startup Methods --
        initializeControls();
        initializeLayout();
        layoutPanes();
        //layoutControls();
        //addListeners();
        applyStylesheet();
        //applySpecialStyles();
    }

    private void initializeControls() {

    }

    private void initializeLayout() {
        circle = new Circle();
        logo = new Button();
    }

    private void layoutPanes() {
        logo.minWidthProperty().bind(logo.prefHeightProperty());
        logo.prefHeightProperty().bind(this.heightProperty());
        circle.radiusProperty().bind(logo.prefHeightProperty());
        logo.prefHeightProperty().addListener((observable, oldValue, newValue) ->
            circle.minWidth(newValue.doubleValue()));
        logo.setShape(circle);

        this.minWidthProperty().bind(logo.prefHeightProperty());
        this.getChildren().add(logo);
    }

    private void layoutControls() {

    }

    private void addListeners() {

    }

    private void applyStylesheet() {

        this.setId("PreviewView");
    }

    public void updateView() {

    }
}
