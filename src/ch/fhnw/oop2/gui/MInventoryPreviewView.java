package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;

/**
 * Created by Lukas on 02.12.2015.
 */
public class MInventoryPreviewView extends VBox implements ViewTemplate{

    private MInventoryObject mInventoryObject;

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private Box box;
    private ImageView cImageView;
    private ImageViewPane imageViewPane;
    private StackPane root;
    private Circle circle;

    public MInventoryPreviewView(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){

        this.presModel = presModel;
        this.dataModel = dataModel;

        this.setAlignment(Pos.CENTER);

        initSequence();
    }


    // --- API ---


    // -- init sequence --
    @Override
    public void initializeControls() {

    }

    @Override
    public void initializeLayout() {
        circle = new Circle();
        root = new StackPane();
            root.setAlignment(Pos.TOP_CENTER);
        cImageView = new CustomImageView(presModel, dataModel);
            //cImageView.setClip(circle); // Buggy: do not use
            ((CustomImageView) cImageView).connectToModel();
        imageViewPane = new ImageViewPane(cImageView);
            circle.setRadius(imageViewPane.getHeight()/2);
            circle.setCenterY(imageViewPane.getHeight()/2);
            circle.setCenterX(imageViewPane.getHeight()/2);
    }

    @Override
    public void layoutPanes() {
        root.getChildren().add(imageViewPane);
        this.getChildren().add(root);
        this.setVgrow(root, Priority.ALWAYS);
        this.setAlignment(Pos.TOP_CENTER);
    }

    @Override
    public void layoutControls() {
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void addBindings() {
    }

    @Override
    public void addEvents() {
    }

    @Override
    public void applyStylesheet() {
        this.setId("PreviewView");
    }

    @Override
    public void applySpecialStyles() {
    }
}
