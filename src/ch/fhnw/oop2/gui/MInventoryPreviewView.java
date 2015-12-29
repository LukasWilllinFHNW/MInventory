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
public class MInventoryPreviewView extends HBox implements ViewTemplate{

    private MInventoryObject mInventoryObject;

    private MInventoryPresentationModel presModel;
    private MInventoryDataModel dataModel;

    private Box box;
    private ImageView cImageView;
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
            circle.setRadius(75);
            circle.setCenterX(75);
            circle.setCenterY(75);
        cImageView = new CustomImageView(presModel, dataModel);
            cImageView.setClip(circle);

    }

    @Override
    public void layoutPanes() {

        this.getChildren().add(cImageView);
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
