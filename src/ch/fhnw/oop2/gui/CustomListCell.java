package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryDataModel;
import ch.fhnw.oop2.model.MInventoryObject;
import ch.fhnw.oop2.model.MInventoryPresentationModel;
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
        super.updateItem(mInventoryObject, empty);
        setGraphic(new Circle(10, 10, 20, Color.WHITESMOKE));
        setText(null);
        // TODO: Remove listener working?
        if (getItem() != null) {
            getItem().getNameProperty().removeListener(nameChangeListener);
            nameChangeListener = null;
        }
        nameChangeListener = (observable, oldValue, newValue) -> {
            this.setText(newValue); };

        if (getItem() != null) {
            setGraphic(new Circle(10, 10, 20, Color.AQUA));
            this.textProperty().setValue( getItem().getName());
            getItem().getNameProperty().addListener(nameChangeListener);
        }
    }

    private void addEvents() {
        this.setOnMouseClicked( (MouseEvent event) -> {
            if (this.getItem() != null) dataModel.updateSelection(this.getItem().getId()); });
    }

    private void addListeners() {
        this.itemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) this.updateItem(newItem, false); });

        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && this.getItem() != null) {
                dataModel.updateSelection(this.getItem().getId());
            } });
    }
}
