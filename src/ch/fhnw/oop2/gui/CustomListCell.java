package ch.fhnw.oop2.gui;

import ch.fhnw.oop2.model.MInventoryObject;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Created by Lukas on 01.12.2015.
 */
public class CustomListCell extends ListCell<MInventoryObject>	{

    @Override
    protected void updateItem(MInventoryObject mInventoryObject, boolean empty)	{
        super.updateItem(mInventoryObject, empty);
        setGraphic(new Circle(10, 10, 20, Color.WHITESMOKE));
        setText(null);
        // TODO: Remove listener working?
        if (getItem() != null) {
            getItem().getNameProperty().removeListener((observable1, oldValue1, newValue1) -> {
                this.setText(oldValue1); } );
        }
        if (getItem() != null) {
            setGraphic(new Circle(10, 10, 20, Color.AQUA));
            this.textProperty().setValue(getItem().getName() );
            //this.textProperty().bind( object.getNameProperty() );
            getItem().getNameProperty().addListener((observable, oldValue, newValue) -> { this.setText(newValue); } );
        }
    }

}
