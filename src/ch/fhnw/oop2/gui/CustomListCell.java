package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Created by Lukas on 01.12.2015.
 */
public class CustomListCell extends ListCell<MInventoryObject>	{

    ChangeListener<String> nameChangeListener;
    ChangeListener<Image> imageChangeListener;

    MInventoryPresentationModel presModel;
    MInventoryDataModel dataModel;

    public CustomListCell(MInventoryPresentationModel presModel, MInventoryDataModel dataModel){
        this.dataModel = dataModel;
        this.presModel = presModel;

        this.setContentDisplay(ContentDisplay.LEFT);

        addListeners();
        addEvents();
    }

    @Override
    protected void updateItem(MInventoryObject mInventoryObject, boolean empty)	{

        if (super.getItem() != null && nameChangeListener != null) {
            getItem().getNameProperty().removeListener(nameChangeListener);
            getItem().getImageProperty().removeListener(imageChangeListener);
        }

        super.updateItem(mInventoryObject, empty);
        super.setGraphic(new Circle(10, 10, 20, Color.WHITESMOKE));
        super.setText(null);

        if (super.getItem() != null) {
            nameChangeListener = (observable, oldValue, newValue) -> {
                setText(newValue); };
            imageChangeListener = (observable, oldValue, newValue) -> {
                updateGraphic(); };
            updateGraphic();
            setText(getItem().getName());
            getItem().getNameProperty().addListener(nameChangeListener);
            getItem().getImageProperty().addListener(imageChangeListener);
        }
    }

    private void addEvents() {
        /*this.setOnMouseClicked( (MouseEvent event) -> {
            if (this.getItem() != null) dataModel.updateSelection(this.getItem().getId()); });*/
        this.setOnDragDropped( event -> {
            if (super.getItem() instanceof MInventoryStorage &&(MInventoryObject)event.getAcceptingObject() != null) {
                try {
                    ((MInventoryStorage) super.getItem()).addObjectById((((MInventoryObject) event.getAcceptingObject()).getId()));
                } catch (MInventoryException e) {

                }
            };
        });
    }

    private void addListeners() {

        super.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && super.getItem() != null) {
                dataModel.updateSelection(super.getItem().getId());
            } });
    }

    private void updateGraphic(){
        if (getItem() != null) {
            ImageView cImageView = new CustomImageView(presModel, dataModel);
            ImageViewPane imageViewPane;
                cImageView.setImage(getItem().getImage());
                cImageView.setPreserveRatio(false);
                cImageView.setFitHeight(42);
                cImageView.setFitWidth(42);
            imageViewPane = new ImageViewPane(cImageView);
                imageViewPane.setMaxHeight(42);
                imageViewPane.setPrefSize(42, 42);
                imageViewPane.setMaxWidth(42);
                cImageView.setClip(new Circle(cImageView.getFitHeight() / 2 - 1, cImageView.getFitWidth() / 2 - 1, 20));
            super.setGraphic(imageViewPane);
        } else {
            ImageView cImageView = new CustomImageView(presModel, dataModel);
            ImageViewPane imageViewPane;
                setStyle("-fx-background-color: whitesmoke;");
                cImageView.setPreserveRatio(false);
                cImageView.setFitHeight(42);
                cImageView.setFitWidth(42);
            imageViewPane = new ImageViewPane(cImageView);
                imageViewPane.setMaxHeight(42);
                imageViewPane.setPrefSize(42, 42);
                imageViewPane.setMaxWidth(42);
                cImageView.setClip(new Circle(cImageView.getFitHeight() / 2 - 1, cImageView.getFitWidth() / 2 - 1, 20));
            super.setGraphic(imageViewPane);
        }
    }
}
