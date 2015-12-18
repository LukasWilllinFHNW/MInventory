package ch.fhnw.oop2.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Lukas W on 03.11.2015.
 */
public class MInventoryPresentationModel {
    private final StringProperty windowTitle = new SimpleStringProperty("MInventory App");
    private final StringProperty buttontext = new SimpleStringProperty("thisisabutton");

    private final BooleanProperty saveDisabledProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty addDisabledProperty = new SimpleBooleanProperty(false);

    private final IntegerProperty heightProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty widthProperty = new SimpleIntegerProperty(0);

    private final IntegerProperty blurProperty = new SimpleIntegerProperty(0);

    private static IntegerProperty x = new SimpleIntegerProperty(0);
    private static IntegerProperty y = new SimpleIntegerProperty(0);

    private ObservableList<MInventoryObject> objectList;
    // public StringProperty getButtonTextProperty() { return this.buttontext; }


    // --- CONSTRUCTORS ---
    /**
     * Create a new MInventoryPresentationModel.
     */
    public MInventoryPresentationModel() {
        this.objectList = FXCollections.observableArrayList();
    }


    // --- GETTER ---
    public ObservableList<MInventoryObject> getObjectList() {
        return objectList;
    }
    public StringProperty getWindowTitleTextProperty() {
        return this.windowTitle;
    }

    public BooleanProperty getSaveDisabledProperty() { return saveDisabledProperty; }
    public BooleanProperty getAddDisabledProperty() { return addDisabledProperty; }

    public double getX() { return x.getValue().doubleValue(); }
    public double getY() { return y.getValue().doubleValue(); }

    public IntegerProperty getHeightProperty() { return heightProperty; }
    public IntegerProperty getWidthProperty() { return widthProperty; }

    public IntegerProperty getBlurProperty() { return blurProperty; }

    // --- SETTER ---
    public void setX(double x) { this.x.setValue(x); }
    public void setY(double y) { this.y.setValue(y); }

    public void doBlur() { this.blurProperty.setValue(80);}
    public void undoBlur() { this.blurProperty.setValue(0);}

    public void enterAddingMode() {
        getSaveDisabledProperty().setValue(false);
        getAddDisabledProperty().setValue(true);
    }

    public void enterEditMode() {
        getSaveDisabledProperty().setValue(true);
        getAddDisabledProperty().setValue(false);
        undoBlur();
    }

}