package ch.fhnw.oop2.model;

import ch.fhnw.oop2.gui.MInventoryUI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;

/**
 * Created by Lukas W on 03.11.2015.
 */
public class MInventoryPresentationModel {

    private Parent mainUI;

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

    public double getX() { return x.getValue().doubleValue(); }
    public double getY() { return y.getValue().doubleValue(); }

    public MInventoryUI getMainUI() {
        return (MInventoryUI) this.mainUI;
    }

    // --- GETTER PROPERTY ---
    public IntegerProperty getHeightProperty() { return heightProperty; }
    public IntegerProperty getWidthProperty() { return widthProperty; }

    public BooleanProperty getSaveDisabledProperty() { return saveDisabledProperty; }
    public BooleanProperty getAddDisabledProperty() { return addDisabledProperty; }

    public IntegerProperty getBlurProperty() { return blurProperty; }

    // --- SETTER ---
    public void setMainUI(Parent mainUI) {
        this.mainUI = mainUI;
    }

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