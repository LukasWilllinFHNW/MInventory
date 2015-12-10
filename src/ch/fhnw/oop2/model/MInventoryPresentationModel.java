package ch.fhnw.oop2.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Lukas W on 03.11.2015.
 */
public class MInventoryPresentationModel {
    private final StringProperty windowTitle = new SimpleStringProperty("MInventory App");
    private final StringProperty buttontext = new SimpleStringProperty("thisisabutton");
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

    // --- SETTER ---

}