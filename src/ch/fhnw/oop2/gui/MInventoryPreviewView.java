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

    @Override
    public void initializeControls() {

    }

    @Override
    public void initializeLayout() {
        circle = new Circle();
        root = new StackPane();
        cImageView = new CustomImageView(presModel, dataModel);
            cImageView.setClip(circle);
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
    }

    @Override
    public void layoutControls() {
    }

    @Override
    public void addListeners() {
        imageViewPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            circle.setRadius(newValue.doubleValue()/2);
            circle.setCenterY(newValue.doubleValue()/2);
            circle.setCenterX(newValue.doubleValue()/2);
        });
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
