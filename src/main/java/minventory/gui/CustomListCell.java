package minventory.gui;

import minventory.model.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Created by Lukas on 01.12.2015.
 */
public class CustomListCell extends ListCell<MInventoryObject>	{

    ChangeListener<String> nameChangeListener;
    ChangeListener<Image> imageChangeListener;
    ChangeListener<Color> colorChangeListener;

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
            getItem().getColorProperty().removeListener(colorChangeListener);
        }

        super.updateItem(mInventoryObject, empty);
        super.setGraphic(new Circle(10, 10, 20, Color.WHITESMOKE));
        super.setText(null);

        if (super.getItem() != null) {
            nameChangeListener = (observable, oldValue, newValue) -> {
                setText(newValue); };
            imageChangeListener = (observable, oldValue, newValue) -> {
                updateGraphic(); };
            colorChangeListener = (observable, oldValue, newValue) -> {
                //Only show color if there is no image
                if (getItem().getImage() == null)
                    super.setGraphic(new Circle(10, 10, 20, getItem().getColor()));
            };

            getItem().getNameProperty().addListener(nameChangeListener);
            getItem().getImageProperty().addListener(imageChangeListener);
            getItem().getColorProperty().addListener(colorChangeListener);

            //Update on update item
            updateGraphic();
            setText(getItem().getName());
        }
    }

    private void addEvents() {
    }

    private void addListeners() {
    }

    private void updateGraphic(){
        if (getItem() != null) {
            if (getItem().getImage() != null) {
                ImageView cImageView = new CustomImageView(presModel, dataModel);
                    cImageView.setImage(getItem().getImage());
                    cImageView.setPreserveRatio(false);
                    cImageView.setFitHeight(42);
                    cImageView.setFitWidth(42);
                ImageViewPane imageViewPane = new ImageViewPane(cImageView);
                    imageViewPane.setMaxHeight(42);
                    imageViewPane.setPrefSize(42, 42);
                    imageViewPane.setMaxWidth(42);
                cImageView.setClip(new Circle(cImageView.getFitHeight() / 2 - 1, cImageView.getFitWidth() / 2 - 1, 20));
                super.setGraphic(imageViewPane);
            } else {
                super.setGraphic(new Circle(10, 10, 20, getItem().getColor()));
            }
        } else {
            super.setGraphic(new Circle(10, 10, 20, Color.WHITESMOKE));
        }
    }
}