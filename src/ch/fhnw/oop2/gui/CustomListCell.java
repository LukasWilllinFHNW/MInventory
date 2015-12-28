package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Created by Lukas on 01.12.2015.
 */
public class CustomListCell extends ListCell<MInventoryObject>	{

    ChangeListener<String> nameChangeListener;
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
            super.getItem().getNameProperty().removeListener(nameChangeListener);
        }

        super.updateItem(mInventoryObject, empty);
        super.setGraphic(new Circle(10, 10, 20, Color.WHITESMOKE));
        super.setText(null);

        if (super.getItem() != null) {
            nameChangeListener = (observable, oldValue, newValue) -> {
                super.setText(newValue); };
            super.setGraphic(new Circle(10, 10, 20, Color.AQUA));
            super.textProperty().setValue( getItem().getName());
            super.getItem().getNameProperty().addListener(nameChangeListener);
        }
    }

    private void addEvents() {
        this.setOnMouseClicked( (MouseEvent event) -> {
            if (this.getItem() != null) dataModel.updateSelection(this.getItem().getId()); });
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
}
