package ch.fhnw.oop2.control;

import ch.fhnw.oop2.model.MInventoryDataModel;
import javafx.beans.property.Property;

/**
 * Class created by Lukas Willin on 03.01.2016.
 * @author Dieter Holz
 */
public class ValueChangeCommand implements Command {
    private final MInventoryDataModel dataModel;
    private final Property property;
    private Object oldValue;
    private Object newValue;

    public ValueChangeCommand(MInventoryDataModel dataModel, Property property, Object oldValue, Object newValue) {
        this.dataModel = dataModel;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public void undo() {
        dataModel.setPropertyValue(property, oldValue);
    }

    public void redo() {
        dataModel.setPropertyValue(property, newValue);
    }

    public void updateRedo(Object newValue) {
        this.newValue = newValue;
    }
    public void updateUndo(Object oldValue) {
        this.oldValue = oldValue;
    }
}
